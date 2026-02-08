"""
Тесты приёма и отправки сообщений через Telegram.
"""
import pytest
import pytest_asyncio
from unittest.mock import AsyncMock, patch
from sqlalchemy import select

from models.message import Message, MessageSource
from models.user import User


# Пример payload от Telegram при отправке пользователем сообщения боту
TELEGRAM_UPDATE_PAYLOAD = {
    "update_id": 123456789,
    "message": {
        "message_id": 1,
        "from": {
            "id": 987654321,
            "is_bot": False,
            "first_name": "Test",
            "username": "test_tg_user",
            "language_code": "ru",
        },
        "chat": {"id": 987654321, "type": "private", "username": "test_tg_user"},
        "date": 1234567890,
        "text": "Привет из Telegram!",
    },
}


@pytest_asyncio.fixture
async def client_with_db():
    """Клиент с подготовленной тестовой БД (таблицы созданы)."""
    from db.session import Base, get_db
    from main import app
    from httpx import AsyncClient, ASGITransport
    from tests.conftest import test_engine, TestSessionLocal

    async def get_test_db():
        async with TestSessionLocal() as session:
            try:
                yield session
            finally:
                await session.close()

    app.dependency_overrides[get_db] = get_test_db

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
async def seed_users(client_with_db):
    """Создать в тестовой БД пользователей admin и bob (с telegram_id). Вызывается после client_with_db."""
    from tests.conftest import TestSessionLocal

    async with TestSessionLocal() as session:
        admin = User(username="admin", hashed_password=None)
        bob = User(username="bob", telegram_id=123456789)
        session.add(admin)
        session.add(bob)
        await session.commit()
    return None


@pytest.mark.asyncio
async def test_telegram_webhook_receive_message(client_with_db):
    """
    Приём сообщения из Telegram через webhook.
    POST /webhook/telegram с payload от ТГ -> создаётся пользователь (если нового нет),
    сообщение сохраняется с source=TELEGRAM.
    """
    response = await client_with_db.post(
        "/webhook/telegram",
        json=TELEGRAM_UPDATE_PAYLOAD,
    )
    assert response.status_code == 200
    assert response.json() == {"status": "ok"}

    # Проверяем, что в БД появились пользователь и сообщение
    from tests.conftest import TestSessionLocal

    async with TestSessionLocal() as session:
        result = await session.execute(select(User).where(User.telegram_id == 987654321))
        user = result.scalars().first()
        assert user is not None
        assert user.username == "test_tg_user"

        result = await session.execute(select(Message).where(Message.source == MessageSource.TELEGRAM))
        messages = result.scalars().all()
        assert len(messages) == 1
        assert messages[0].content == "Привет из Telegram!"
        assert messages[0].sender_id == user.id
        assert messages[0].receiver_id is None


@pytest.mark.asyncio
async def test_telegram_webhook_second_message_same_user(client_with_db):
    """Повторное сообщение от того же пользователя ТГ — не создаём нового пользователя."""
    await client_with_db.post("/webhook/telegram", json=TELEGRAM_UPDATE_PAYLOAD)
    payload2 = {
        **TELEGRAM_UPDATE_PAYLOAD,
        "message": {
            **TELEGRAM_UPDATE_PAYLOAD["message"],
            "message_id": 2,
            "text": "Второе сообщение",
        },
    }
    await client_with_db.post("/webhook/telegram", json=payload2)

    from tests.conftest import TestSessionLocal

    async with TestSessionLocal() as session:
        result = await session.execute(select(User).where(User.telegram_id == 987654321))
        users = result.scalars().all()
        assert len(users) == 1
        result = await session.execute(select(Message).where(Message.source == MessageSource.TELEGRAM))
        messages = result.scalars().all()
        assert len(messages) == 2


@pytest.mark.asyncio
async def test_send_message_to_telegram_user(client_with_db, seed_users):
    """
    Отправка сообщения через мессенджер пользователю с telegram_id —
    бот должен отправить сообщение в ТГ получателю (вызов send_telegram_message с chat_id получателя).
    """
    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock) as mock_send:
        mock_send.return_value = True

        response = await client_with_db.post(
            "/messages/send/admin/bob",
            json={"content": "Привет из мессенджера!"},
        )

    assert response.status_code == 200
    data = response.json()
    assert data["content"] == "Привет из мессенджера!"
    assert data["source"] == MessageSource.OWN_MESSENGER.value

    # Бот должен был отправить сообщение в ТГ получателю (bob.telegram_id = 123456789)
    mock_send.assert_called_once()
    call_kwargs = mock_send.call_args.kwargs
    assert call_kwargs["chat_id"] == 123456789
    assert call_kwargs["text"] == "Привет из мессенджера!"


@pytest.mark.asyncio
async def test_send_message_to_user_without_telegram_no_api_call(client_with_db, seed_users):
    """
    Отправка сообщения пользователю без telegram_id — send_telegram_message не вызывается.
    """
    from tests.conftest import TestSessionLocal

    # Создаём пользователя без telegram_id
    async with TestSessionLocal() as session:
        alice = User(username="alice", telegram_id=None)
        session.add(alice)
        await session.commit()

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock) as mock_send:
        response = await client_with_db.post(
            "/messages/send/admin/alice",
            json={"content": "Только в мессенджер"},
        )

    assert response.status_code == 200
    mock_send.assert_not_called()
