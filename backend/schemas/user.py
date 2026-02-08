from pydantic import BaseModel

class UserBase(BaseModel):
    username: str

class UserCreate(UserBase):
    password: str

class UserOut(UserBase):
    id: int
    telegram_id: int | None = None
    class Config:
        from_attributes = True  # Для Pydantic v2, совместимо с orm_mode

class UserTelegramLink(BaseModel):
    """Привязка Telegram к пользователю приложения."""
    telegram_id: int