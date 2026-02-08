from fastapi import FastAPI
from api import user, message, integration, websocket
from db.session import engine, Base
from core.config import settings
from services.telegram_service import set_telegram_webhook
# Импортируем модели, чтобы они зарегистрировались в Base.metadata
from models import User, Message, Integration  # noqa: F401

app = FastAPI(title="Messenger Backend", version="1.0.0")

@app.on_event("startup")
async def init_db():
    """Создает таблицы в БД при старте приложения."""
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)


@app.on_event("startup")
async def register_telegram_webhook():
    """Если заданы TG_BOT_TOKEN и BASE_URL — регистрирует webhook для приёма сообщений из Telegram."""
    if not settings.TG_BOT_TOKEN or not settings.BASE_URL:
        return
    webhook_url = f"{settings.BASE_URL}/webhook/telegram"
    ok = await set_telegram_webhook(webhook_url)
    if ok:
        print(f"Telegram webhook registered: {webhook_url}")
    else:
        print("Telegram webhook registration failed (check TG_BOT_TOKEN and BASE_URL)")

app.include_router(user.router, prefix="/users", tags=["Users"])
app.include_router(message.router, prefix="/messages", tags=["Messages"])
app.include_router(integration.router, tags=["Integrations"])
app.include_router(websocket.router)