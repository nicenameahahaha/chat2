import aiohttp
from core.config import settings

async def send_telegram_message(chat_id: int, text: str) -> bool:
    """Отправляет сообщение в Telegram через Bot API."""
    if not settings.TG_BOT_TOKEN:
        return False

    url = f"https://api.telegram.org/bot{settings.TG_BOT_TOKEN}/sendMessage"
    payload = {"chat_id": chat_id, "text": text}
    try:
        async with aiohttp.ClientSession() as session:
            async with session.post(url, json=payload) as response:
                return response.status == 200
    except Exception:
        return False


async def set_telegram_webhook(webhook_url: str) -> bool:
    """
    Регистрирует webhook в Telegram (куда присылать входящие сообщения).
    webhook_url должен быть полным, например: https://your-domain.com/webhook/telegram
    """
    if not settings.TG_BOT_TOKEN:
        return False
    url = f"https://api.telegram.org/bot{settings.TG_BOT_TOKEN}/setWebhook"
    try:
        async with aiohttp.ClientSession() as session:
            async with session.post(url, data={"url": webhook_url}) as response:
                return response.status == 200
    except Exception:
        return False
