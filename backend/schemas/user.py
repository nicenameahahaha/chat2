from pydantic import BaseModel, ConfigDict

class UserBase(BaseModel):
    username: str

class UserCreate(UserBase):
    password: str

class UserOut(UserBase):
    model_config = ConfigDict(from_attributes=True)
    id: int
    telegram_id: int | None = None

class UserTelegramLink(BaseModel):
    """Привязка Telegram к пользователю приложения."""
    telegram_id: int
