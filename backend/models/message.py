from sqlalchemy import Column, Integer, String, ForeignKey, DateTime, Boolean, Enum
from sqlalchemy.orm import relationship
from datetime import datetime
from db.session import Base
import enum


class MessageSource(str, enum.Enum):
    """Источник сообщения"""
    OWN_MESSENGER = "own_messenger"  # Собственный мессенджер
    TELEGRAM = "telegram"  # Telegram интеграция


class Message(Base):
    __tablename__ = "messages"

    id = Column(Integer, primary_key=True)
    # Отправитель сообщения (пользователь системы или телеграм‑пользователь)
    sender_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    # Получатель сообщения в нашем собственном мессенджере (может быть NULL для общих сообщений/интеграций)
    receiver_id = Column(Integer, ForeignKey("users.id"), nullable=True)

    content = Column(String, nullable=False)
    timestamp = Column(DateTime, default=datetime.utcnow)
    
    # Статусы сообщения (для собственного мессенджера)
    is_delivered = Column(Boolean, default=False, nullable=False)  # Доставлено ли сообщение
    is_read = Column(Boolean, default=False, nullable=False)  # Прочитано ли сообщение
    read_at = Column(DateTime, nullable=True)  # Время прочтения
    
    # Источник сообщения
    source = Column(Enum(MessageSource), default=MessageSource.OWN_MESSENGER, nullable=False)

    # Связи с пользователями
    sender = relationship(
        "User",
        back_populates="messages_sent",
        foreign_keys=[sender_id],
    )
    receiver = relationship(
        "User",
        back_populates="messages_received",
        foreign_keys=[receiver_id],
    )
