import os
from dotenv import load_dotenv

load_dotenv()

class Settings:
    def __init__(self):
        self.TG_BOT_TOKEN: str = os.getenv("TG_BOT_TOKEN", "")
        # Публичный URL бэкенда (нужен для регистрации webhook Telegram). Пример: https://your-domain.com
        self.BASE_URL: str = os.getenv("BASE_URL", "").rstrip("/")
        self.DATABASE_URL: str = os.getenv("DATABASE_URL", "sqlite+aiosqlite:///./messenger.db")
        self.DEBUG: bool = os.getenv("DEBUG", "True").lower() == "true"
        
        if not self.DATABASE_URL:
            raise ValueError("DATABASE_URL must be set")

settings = Settings()