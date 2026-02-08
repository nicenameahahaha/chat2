from datetime import datetime
from pydantic import BaseModel
from models.message import MessageSource


class MessageBase(BaseModel):
    content: str


class MessageCreate(MessageBase):
    """
    Базовая схема создания сообщения.
    Может использоваться как для интеграции (Telegram), так и для простого
    отправки сообщения «от системы».
    """

    # Для собственного мессенджера можно указать получателя по username
    receiver_username: str | None = None


class MessageOut(MessageBase):
    id: int
    sender_id: int
    receiver_id: int | None = None
    timestamp: datetime
    is_delivered: bool = False
    is_read: bool = False
    read_at: datetime | None = None
    source: MessageSource = MessageSource.OWN_MESSENGER

    class Config:
        from_attributes = True  # Для Pydantic v2, совместимо с orm_mode


class MessageStatusUpdate(BaseModel):
    """Схема для обновления статуса сообщения"""
    is_delivered: bool | None = None
    is_read: bool | None = None
