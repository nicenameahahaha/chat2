from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select
from models.user import User
from schemas.user import UserCreate
from core.security import get_password_hash

async def get_user_by_username(db: AsyncSession, username: str):
    result = await db.execute(select(User).where(User.username == username))
    return result.scalars().first()

async def create_user(db: AsyncSession, user_in: UserCreate):
    user = User(
        username=user_in.username,
        hashed_password=get_password_hash(user_in.password)
    )
    db.add(user)
    await db.commit()
    await db.refresh(user)
    return user

async def get_user_by_telegram_id(db: AsyncSession, telegram_id: int):
    result = await db.execute(select(User).where(User.telegram_id == telegram_id))
    return result.scalars().first()

async def create_telegram_user(db: AsyncSession, telegram_id: int, username: str | None):
    user = User(
        telegram_id=telegram_id,
        username=username or f"tg_{telegram_id}"
    )
    db.add(user)
    await db.commit()
    await db.refresh(user)
    return user


async def link_telegram_to_user(db: AsyncSession, username: str, telegram_id: int) -> User | None:
    """
    Привязывает telegram_id к существующему пользователю приложения.
    Если этот telegram_id был у другого пользователя — снимает привязку у того.
    """
    user = await get_user_by_username(db, username)
    if not user:
        return None
    other = await get_user_by_telegram_id(db, telegram_id)
    if other and other.id != user.id:
        other.telegram_id = None
    user.telegram_id = telegram_id
    await db.commit()
    await db.refresh(user)
    return user
