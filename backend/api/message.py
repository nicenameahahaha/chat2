from typing import List

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession

from db.session import get_db
from schemas.message import MessageCreate, MessageOut, MessageStatusUpdate
from services.message_service import (
    create_own_messenger_message,
    get_messages_received,
    get_messages_sent,
    get_all_user_messages,
    get_conversation,
    mark_message_as_read,
    mark_message_as_delivered,
    get_message_by_id,
)
from services.user_service import get_user_by_username

router = APIRouter()


@router.post("/", response_model=MessageOut)
async def send_message(
    message_in: MessageCreate,
    db: AsyncSession = Depends(get_db),
):
    """
    Отправка сообщения в собственный мессенджер.

    Сейчас вместо аутентификации используется фиксированный пользователь "admin"
    как отправитель, чтобы не усложнять пример.

    - Если указать receiver_username — сообщение пойдёт в наш собственный
      мессенджер к указанному пользователю.
    - Если receiver_username не указан — сообщение может использоваться
      как системное/групповое.
    """
    sender = await get_user_by_username(db, "admin")
    if not sender:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Sender user not found"
        )

    receiver = None
    if message_in.receiver_username:
        receiver = await get_user_by_username(db, message_in.receiver_username)
        if not receiver:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Receiver user not found",
            )
        
        # Используем функцию для собственного мессенджера
        message = await create_own_messenger_message(
            db,
            sender=sender,
            receiver=receiver,
            content=message_in.content,
        )
    else:
        # Для сообщений без получателя используем старую функцию
        from services.message_service import create_message
        from models.message import MessageSource
        message = await create_message(
            db,
            sender=sender,
            content=message_in.content,
            receiver=None,
            source=MessageSource.OWN_MESSENGER,
        )
    
    return message


@router.post("/send/{sender_username}/{receiver_username}", response_model=MessageOut)
async def send_own_messenger_message(
    sender_username: str,
    receiver_username: str,
    message_in: MessageCreate,
    db: AsyncSession = Depends(get_db),
):
    """
    Отправка сообщения в собственный мессенджер от одного пользователя другому.
    Автоматически помечает сообщение как доставленное.
    """
    sender = await get_user_by_username(db, sender_username)
    if not sender:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Sender '{sender_username}' not found",
        )
    
    receiver = await get_user_by_username(db, receiver_username)
    if not receiver:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Receiver '{receiver_username}' not found",
        )
    
    message = await create_own_messenger_message(
        db,
        sender=sender,
        receiver=receiver,
        content=message_in.content,
    )
    return message


@router.get("/sent/{username}", response_model=List[MessageOut])
async def get_sent_messages(
    username: str,
    db: AsyncSession = Depends(get_db),
):
    """
    **Проверка отправленных сообщений.**

    Возвращает все сообщения, где пользователь является `sender`
    (и в Telegram‑интеграции, и в собственном мессенджере).
    """
    user = await get_user_by_username(db, username)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="User not found"
        )
    messages = await get_messages_sent(db, user)
    return messages


@router.get("/received/{username}", response_model=List[MessageOut])
async def get_received_messages(
    username: str,
    db: AsyncSession = Depends(get_db),
):
    """
    **Проверка полученных сообщений в собственном мессенджере.**

    Возвращает все сообщения, где пользователь указан как `receiver`.
    Для Telegram‑интеграции `receiver_id` обычно не задаётся.
    """
    user = await get_user_by_username(db, username)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="User not found"
        )
    messages = await get_messages_received(db, user)
    return messages


@router.get("/all/{username}")
async def get_all_messages(
    username: str,
    db: AsyncSession = Depends(get_db),
):
    """
    **Получить все сообщения пользователя (отправленные и полученные).**
    
    Возвращает словарь с ключами 'sent' и 'received'.
    """
    user = await get_user_by_username(db, username)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="User not found"
        )
    messages = await get_all_user_messages(db, user)
    return {
        "sent": messages["sent"],
        "received": messages["received"],
    }


@router.get("/conversation/{username1}/{username2}", response_model=List[MessageOut])
async def get_conversation_messages(
    username1: str,
    username2: str,
    db: AsyncSession = Depends(get_db),
):
    """
    **Получить переписку между двумя пользователями в собственном мессенджере.**
    
    Возвращает все сообщения между указанными пользователями, отсортированные по времени.
    """
    user1 = await get_user_by_username(db, username1)
    user2 = await get_user_by_username(db, username2)
    
    if not user1:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"User '{username1}' not found",
        )
    if not user2:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"User '{username2}' not found",
        )
    
    messages = await get_conversation(db, user1.id, user2.id)
    return messages


@router.get("/{message_id}", response_model=MessageOut)
async def get_message(
    message_id: int,
    db: AsyncSession = Depends(get_db),
):
    """Получить сообщение по ID."""
    message = await get_message_by_id(db, message_id)
    if not message:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Message not found",
        )
    return message


@router.patch("/{message_id}/read/{username}", response_model=MessageOut)
async def mark_as_read(
    message_id: int,
    username: str,
    db: AsyncSession = Depends(get_db),
):
    """
    **Пометить сообщение как прочитанное.**
    
    Пользователь должен быть получателем сообщения.
    """
    user = await get_user_by_username(db, username)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found",
        )
    
    message = await mark_message_as_read(db, message_id, user)
    if not message:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Message not found or user is not the receiver",
        )
    return message


@router.patch("/{message_id}/delivered", response_model=MessageOut)
async def mark_as_delivered(
    message_id: int,
    db: AsyncSession = Depends(get_db),
):
    """
    **Пометить сообщение как доставленное.**
    """
    message = await mark_message_as_delivered(db, message_id)
    if not message:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Message not found",
        )
    return message
