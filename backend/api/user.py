from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from schemas.user import UserCreate, UserOut, UserTelegramLink
from services.user_service import create_user, get_user_by_username, link_telegram_to_user
from db.session import get_db

router = APIRouter()

@router.post("/", response_model=UserOut, status_code=status.HTTP_201_CREATED)
async def register_user(user_in: UserCreate, db: AsyncSession = Depends(get_db)):
    existing_user = await get_user_by_username(db, user_in.username)
    if existing_user:
        raise HTTPException(status_code=400, detail="Username already registered")
    user = await create_user(db, user_in)
    return user


@router.patch("/{username}/telegram", response_model=UserOut)
async def link_user_telegram(
    username: str,
    body: UserTelegramLink,
    db: AsyncSession = Depends(get_db),
):
    """
    Привязать Telegram к пользователю приложения.
    После привязки этому пользователю можно отправлять сообщения в ТГ из мессенджера.
    telegram_id можно узнать, когда пользователь напишет боту (он создастся с этим id в БД).
    """
    user = await link_telegram_to_user(db, username, body.telegram_id)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
    return user
