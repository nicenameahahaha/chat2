from contextlib import asynccontextmanager
from fastapi import FastAPI
from api import user, message, integration, websocket
from db.session import engine, Base
from core.config import settings
from services.telegram_service import set_telegram_webhook
# Импортируем модели, чтобы они зарегистрировались в Base.metadata
from models import User, Message, Integration  # noqa: F401


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Создание таблиц в БД и регистрация webhook Telegram при старте."""
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
    if settings.TG_BOT_TOKEN and settings.BASE_URL:
        webhook_url = f"{settings.BASE_URL}/webhook/telegram"
        ok = await set_telegram_webhook(webhook_url)
        if ok:
            print(f"Telegram webhook registered: {webhook_url}")
        else:
            print("Telegram webhook registration failed (check TG_BOT_TOKEN and BASE_URL)")
    yield


app = FastAPI(title="Messenger Backend", version="1.0.0", lifespan=lifespan)

app.include_router(user.router, prefix="/users", tags=["Users"])
app.include_router(message.router, prefix="/messages", tags=["Messages"])
app.include_router(integration.router, tags=["Integrations"])
app.include_router(websocket.router)
