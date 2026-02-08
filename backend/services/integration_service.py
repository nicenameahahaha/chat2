import re
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select
from models.integration import Integration
from models.message import MessageSource
from services.user_service import get_user_by_telegram_id, create_telegram_user, get_user_by_username, link_telegram_to_user
from services.message_service import create_message
from services.telegram_service import send_telegram_message
from core.ws_manager import manager


def _parse_telegram_addressee(text: str) -> tuple[str | None, str]:
    """
    Извлекает получателя из текста сообщения в ТГ.
    Форматы: "@username текст" или "username: текст".
    Возвращает (username_получателя или None, текст_сообщения).
    """
    text = text.strip()
    if not text:
        return None, text
    # @username остаток
    m = re.match(r"^@(\w+)\s+(.+)$", text, re.DOTALL)
    if m:
        return m.group(1).strip(), m.group(2).strip()
    # username: остаток
    m = re.match(r"^(\w+):\s*(.+)$", text, re.DOTALL)
    if m:
        return m.group(1).strip(), m.group(2).strip()
    return None, text


async def get_integration(db: AsyncSession, service_name: str):
    result = await db.execute(select(Integration).where(Integration.service_name == service_name))
    return result.scalars().first()

async def create_integration(db: AsyncSession, service_name: str, api_token: str):
    integration = Integration(service_name=service_name, api_token=api_token)
    db.add(integration)
    await db.commit()
    await db.refresh(integration)
    return integration

async def handle_telegram_update(db: AsyncSession, update: dict):
    """
    Обработка обновлений из Telegram.
    - Команда /start — приветствие и инструкция.
    - Команда /start username — привязка Telegram к пользователю приложения (username).
    - Если текст в формате «@username текст» или «username: текст» — сообщение доставляется
      пользователю username: сохраняется в БД, отправляется ему в ТГ (если привязан) и в приложение по WebSocket.
    - Иначе — сохраняется как входящее из ТГ без получателя и рассылается всем подключённым.
    """
    if "message" not in update or "text" not in update["message"]:
        return

    tg_msg = update["message"]
    tg_user_info = tg_msg["from"]
    text = (tg_msg["text"] or "").strip()
    telegram_id = tg_user_info["id"]
    tg_username = tg_user_info.get("username")
    chat_id = tg_msg["chat"]["id"]

    # Обработка команды /start для привязки аккаунта
    if text == "/start":
        sender = await get_user_by_telegram_id(db, telegram_id)
        if not sender:
            sender = await create_telegram_user(db, telegram_id, tg_username)
        await send_telegram_message(
            chat_id,
            "Добро пожаловать! Чтобы привязать аккаунт приложения, отправьте:\n/start ваш_username_в_приложении",
        )
        return
    if text.startswith("/start "):
        app_username = text[7:].strip()
        if app_username:
            app_user = await get_user_by_username(db, app_username)
            if app_user:
                await link_telegram_to_user(db, app_username, telegram_id)
                await send_telegram_message(chat_id, f"Аккаунт привязан к пользователю {app_username}.")
            else:
                await send_telegram_message(
                    chat_id,
                    f"Пользователь «{app_username}» не найден. Зарегистрируйтесь в приложении и попробуйте снова.",
                )
        else:
            await send_telegram_message(chat_id, "Укажите username: /start ваш_username")
        return

    sender = await get_user_by_telegram_id(db, telegram_id)
    if not sender:
        sender = await create_telegram_user(db, telegram_id, tg_username)

    receiver_username, content = _parse_telegram_addressee(text)
    receiver = None
    if receiver_username:
        receiver = await get_user_by_username(db, receiver_username)

    if receiver and content:
        # Личное сообщение А → Б: сохраняем с получателем
        message = await create_message(
            db, sender=sender, receiver=receiver, content=content, source=MessageSource.TELEGRAM
        )
        # Доставляем Б в Telegram, если у него привязан ТГ
        if receiver.telegram_id:
            send_text = f"От {sender.username} (ТГ): {content}"
            await send_telegram_message(chat_id=receiver.telegram_id, text=send_text)
        # Уведомляем в приложении: рассылаем всем, в payload есть receiver — клиент Б может показать у себя
        await manager.broadcast({
            "type": "telegram_message",
            "message_id": message.id,
            "sender": sender.username,
            "sender_telegram_id": telegram_id,
            "receiver": receiver.username,
            "content": content,
            "timestamp": message.timestamp.isoformat() if message.timestamp else None,
            "source": MessageSource.TELEGRAM.value,
        })
    else:
        # Без указания получателя — как раньше: общее входящее из ТГ
        if not content and receiver_username:
            content = text
        message = await create_message(db, sender=sender, content=content, source=MessageSource.TELEGRAM)
        await manager.broadcast({
            "type": "telegram_message",
            "message_id": message.id,
            "sender": sender.username,
            "sender_telegram_id": telegram_id,
            "content": content,
            "timestamp": message.timestamp.isoformat() if message.timestamp else None,
            "source": MessageSource.TELEGRAM.value,
        })
