from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from datetime import datetime

from models.message import Message, MessageSource
from models.user import User
from services.telegram_service import send_telegram_message


async def create_message(
    db: AsyncSession,
    sender: User,
    content: str,
    receiver: User | None = None,
    source: MessageSource = MessageSource.OWN_MESSENGER,
) -> Message:
    """
    Универсальное создание сообщения.

    - Если указан receiver и source=OWN_MESSENGER — это сообщение в нашем собственном мессенджере
      от sender к receiver. Статус доставки устанавливается автоматически при отправке.
    - Если receiver не указан или source=TELEGRAM — это сообщения из интеграций.
    """
    message = Message(
        sender_id=sender.id,
        receiver_id=receiver.id if receiver else None,
        content=content,
        source=source,
        # Для собственного мессенджера с получателем - автоматически помечаем как доставленное
        is_delivered=True if (receiver and source == MessageSource.OWN_MESSENGER) else False,
    )

    db.add(message)
    await db.commit()
    await db.refresh(message)

    # Отправка в Telegram получателю (если у получателя привязан ТГ и это не входящее из ТГ)
    if source != MessageSource.TELEGRAM and receiver and receiver.telegram_id:
        await send_telegram_message(chat_id=receiver.telegram_id, text=message.content)

    return message


async def create_own_messenger_message(
    db: AsyncSession,
    sender: User,
    receiver: User,
    content: str,
) -> Message:
    """
    Создание сообщения в собственном мессенджере между двумя пользователями.
    Автоматически помечает сообщение как доставленное.
    """
    return await create_message(
        db=db,
        sender=sender,
        receiver=receiver,
        content=content,
        source=MessageSource.OWN_MESSENGER,
    )


async def get_messages_sent(db: AsyncSession, user: User) -> list[Message]:
    """Все сообщения, отправленные указанным пользователем (наш мессенджер + интеграции)."""
    result = await db.execute(select(Message).where(Message.sender_id == user.id))
    return list(result.scalars().all())


async def get_messages_received(db: AsyncSession, user: User) -> list[Message]:
    """
    Все сообщения, полученные пользователем в нашем собственном мессенджере.
    Для интеграций receiver_id, как правило, не заполняется.
    """
    result = await db.execute(select(Message).where(Message.receiver_id == user.id))
    return list(result.scalars().all())


async def get_all_user_messages(db: AsyncSession, user: User) -> dict:
    """
    Получить все сообщения пользователя (отправленные и полученные).
    Возвращает словарь с ключами 'sent' и 'received'.
    """
    sent = await get_messages_sent(db, user)
    received = await get_messages_received(db, user)
    return {"sent": sent, "received": received}


async def get_message_by_id(db: AsyncSession, message_id: int) -> Message | None:
    """Получить сообщение по ID."""
    result = await db.execute(select(Message).where(Message.id == message_id))
    return result.scalars().first()


async def mark_message_as_delivered(db: AsyncSession, message_id: int) -> Message | None:
    """Пометить сообщение как доставленное."""
    message = await get_message_by_id(db, message_id)
    if message:
        message.is_delivered = True
        await db.commit()
        await db.refresh(message)
    return message


async def mark_message_as_read(db: AsyncSession, message_id: int, user: User) -> Message | None:
    """
    Пометить сообщение как прочитанное.
    Проверяет, что пользователь является получателем сообщения.
    """
    message = await get_message_by_id(db, message_id)
    if message and message.receiver_id == user.id:
        message.is_read = True
        message.read_at = datetime.utcnow()
        await db.commit()
        await db.refresh(message)
        return message
    return None


async def get_conversation(
    db: AsyncSession,
    user1_id: int,
    user2_id: int,
) -> list[Message]:
    """
    Получить переписку между двумя пользователями в собственном мессенджере.
    Возвращает все сообщения, где один пользователь - отправитель, другой - получатель.
    """
    result = await db.execute(
        select(Message)
        .where(
            ((Message.sender_id == user1_id) & (Message.receiver_id == user2_id)) |
            ((Message.sender_id == user2_id) & (Message.receiver_id == user1_id))
        )
        .where(Message.source == MessageSource.OWN_MESSENGER)
        .order_by(Message.timestamp)
    )
    return list(result.scalars().all())

