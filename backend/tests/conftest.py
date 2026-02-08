"""
Фикстуры для тестов: тестовая БД и переопределение get_db.
"""
import pytest
import pytest_asyncio
from httpx import AsyncClient, ASGITransport
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker

from sqlalchemy.pool import StaticPool

from db.session import Base, get_db
from main import app
from models.user import User


# Тестовая БД в памяти. StaticPool — одно соединение, все сессии видят одну БД.
TEST_DATABASE_URL = "sqlite+aiosqlite:///:memory:"

test_engine = create_async_engine(
    TEST_DATABASE_URL,
    echo=False,
    poolclass=StaticPool,
)
TestSessionLocal = sessionmaker(
    bind=test_engine, class_=AsyncSession, expire_on_commit=False
)


async def override_get_db():
    async with TestSessionLocal() as session:
        try:
            yield session
        finally:
            await session.close()


@pytest_asyncio.fixture(scope="function")
async def db_session():
    """Создать таблицы и сессию для каждого теста."""
    async with test_engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
    async with TestSessionLocal() as session:
        yield session
    async with test_engine.begin() as conn:
        await conn.run_sync(Base.metadata.drop_all)


@pytest_asyncio.fixture(scope="function")
async def client(db_session):
    """HTTP-клиент с подменённой БД."""
    app.dependency_overrides[get_db] = override_get_db

    async def get_test_db():
        async with TestSessionLocal() as session:
            try:
                yield session
            finally:
                await session.close()

    app.dependency_overrides[get_db] = get_test_db

    # Инициализируем таблицы для этого теста
    async with test_engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)

    async with AsyncClient(
        transport=ASGITransport(app=app),
        base_url="http://test",
    ) as ac:
        yield ac

    app.dependency_overrides.clear()

    async with test_engine.begin() as conn:
        await conn.run_sync(Base.metadata.drop_all)


@pytest_asyncio.fixture
async def admin_user(db_session):
    """Пользователь admin для отправки сообщений."""
    user = User(username="admin", hashed_password=None)
    db_session.add(user)
    await db_session.commit()
    await db_session.refresh(user)
    return user


@pytest_asyncio.fixture
async def telegram_user(db_session):
    """Пользователь с привязанным Telegram (получатель в ТГ)."""
    user = User(username="bob", telegram_id=123456789)
    db_session.add(user)
    await db_session.commit()
    await db_session.refresh(user)
    return user
