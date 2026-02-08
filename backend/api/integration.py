from fastapi import APIRouter, Request, Depends
from sqlalchemy.ext.asyncio import AsyncSession
from services.integration_service import handle_telegram_update
from db.session import get_db

router = APIRouter()

@router.post("/webhook/telegram")
async def telegram_webhook(request: Request, db: AsyncSession = Depends(get_db)):
    payload = await request.json()
    await handle_telegram_update(db, payload)
    return {"status": "ok"}
