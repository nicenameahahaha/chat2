"""
Тесты API: пользователи, сообщения, интеграции.
"""
import pytest
import pytest_asyncio
from httpx import AsyncClient, ASGITransport
from sqlalchemy import select

from main import app
from db.session import Base, get_db
from models.user import User
from models.message import Message, MessageSource
from tests.conftest import test_engine, TestSessionLocal


async def _get_test_db():
    async with TestSessionLocal() as session:
        try:
            yield session
        finally:
            await session.close()


@pytest_asyncio.fixture
async def client_with_db():
    """Клиент с подготовленной тестовой БД."""
    app.dependency_overrides[get_db] = _get_test_db

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
async def seed_data(client_with_db):
    """Требует client_with_db для порядка инициализации."""
    """Создать пользователей admin, bob, alice в тестовой БД."""
    async with TestSessionLocal() as session:
        admin = User(username="admin", hashed_password="hash")
        bob = User(username="bob", telegram_id=123456789)
        alice = User(username="alice", telegram_id=None)
        session.add_all([admin, bob, alice])
        await session.commit()


# ============ APP ============

@pytest.mark.asyncio
async def test_app_openapi(client_with_db):
    """Проверка, что приложение отдаёт OpenAPI schema."""
    response = await client_with_db.get("/openapi.json")
    assert response.status_code == 200
    data = response.json()
    assert "openapi" in data
    assert "paths" in data


# ============ USERS ============

@pytest.mark.asyncio
async def test_register_user(client_with_db):
    """Регистрация нового пользователя."""
    response = await client_with_db.post(
        "/users/",
        json={"username": "newuser", "password": "secret123"},
    )
    assert response.status_code == 201
    data = response.json()
    assert data["username"] == "newuser"
    assert data["id"]
    assert "password" not in data


@pytest.mark.asyncio
async def test_register_user_duplicate(client_with_db, seed_data):
    """Регистрация с существующим username — ошибка 400."""
    response = await client_with_db.post(
        "/users/",
        json={"username": "admin", "password": "any"},
    )
    assert response.status_code == 400
    assert "already registered" in response.json()["detail"].lower()


@pytest.mark.asyncio
async def test_link_telegram(client_with_db, seed_data):
    """Привязка Telegram к пользователю."""
    response = await client_with_db.patch(
        "/users/alice/telegram",
        json={"telegram_id": 999888777},
    )
    assert response.status_code == 200
    assert response.json()["telegram_id"] == 999888777
    assert response.json()["username"] == "alice"


@pytest.mark.asyncio
async def test_link_telegram_user_not_found(client_with_db):
    """Привязка Telegram к несуществующему пользователю — 404."""
    response = await client_with_db.patch(
        "/users/nonexistent/telegram",
        json={"telegram_id": 111},
    )
    assert response.status_code == 404


# ============ MESSAGES ============

@pytest.mark.asyncio
async def test_send_message_sender_not_found(client_with_db):
    """POST /messages/ — admin не создан, 404."""
    response = await client_with_db.post(
        "/messages/",
        json={"content": "Hi", "receiver_username": "bob"},
    )
    assert response.status_code == 404
    assert "sender" in response.json()["detail"].lower()


@pytest.mark.asyncio
async def test_send_message_receiver_not_found(client_with_db, seed_data):
    """POST /messages/ — получатель не найден."""
    response = await client_with_db.post(
        "/messages/",
        json={"content": "Hi", "receiver_username": "nonexistent"},
    )
    assert response.status_code == 404
    assert "receiver" in response.json()["detail"].lower()


@pytest.mark.asyncio
async def test_send_message_without_receiver(client_with_db, seed_data):
    """POST /messages/ без receiver_username — системное/групповое сообщение."""
    response = await client_with_db.post(
        "/messages/",
        json={"content": "System announcement"},
    )
    assert response.status_code == 200
    data = response.json()
    assert data["content"] == "System announcement"
    assert data["receiver_id"] is None
    assert data["source"] == MessageSource.OWN_MESSENGER.value


@pytest.mark.asyncio
async def test_send_message_success(client_with_db, seed_data):
    """POST /messages/ — успешная отправка."""
    response = await client_with_db.post(
        "/messages/",
        json={"content": "Hello Bob!", "receiver_username": "bob"},
    )
    assert response.status_code == 200
    data = response.json()
    assert data["content"] == "Hello Bob!"
    assert data["source"] == MessageSource.OWN_MESSENGER.value
    assert data["receiver_id"]
    assert data["sender_id"]
    assert data["is_delivered"] is True


@pytest.mark.asyncio
async def test_send_message_via_path(client_with_db, seed_data):
    """POST /messages/send/{sender}/{receiver} — успешная отправка."""
    response = await client_with_db.post(
        "/messages/send/admin/bob",
        json={"content": "Direct path message"},
    )
    assert response.status_code == 200
    assert response.json()["content"] == "Direct path message"


@pytest.mark.asyncio
async def test_send_message_sender_not_found_path(client_with_db, seed_data):
    """POST /messages/send/{sender}/{receiver} — отправитель не найден."""
    response = await client_with_db.post(
        "/messages/send/nobody/bob",
        json={"content": "Hi"},
    )
    assert response.status_code == 404


@pytest.mark.asyncio
async def test_get_sent_messages(client_with_db, seed_data):
    """GET /messages/sent/{username} — список отправленных."""
    # Сначала отправим сообщение
    await client_with_db.post(
        "/messages/send/admin/bob",
        json={"content": "Test sent"},
    )
    response = await client_with_db.get("/messages/sent/admin")
    assert response.status_code == 200
    data = response.json()
    assert isinstance(data, list)
    assert len(data) >= 1
    assert any(m["content"] == "Test sent" for m in data)


@pytest.mark.asyncio
async def test_get_received_messages(client_with_db, seed_data):
    """GET /messages/received/{username} — список полученных."""
    await client_with_db.post(
        "/messages/send/admin/bob",
        json={"content": "For Bob"},
    )
    response = await client_with_db.get("/messages/received/bob")
    assert response.status_code == 200
    data = response.json()
    assert isinstance(data, list)
    assert len(data) >= 1
    assert any(m["content"] == "For Bob" for m in data)


@pytest.mark.asyncio
async def test_get_all_messages(client_with_db, seed_data):
    """GET /messages/all/{username} — все сообщения."""
    await client_with_db.post(
        "/messages/send/admin/bob",
        json={"content": "All test"},
    )
    response = await client_with_db.get("/messages/all/bob")
    assert response.status_code == 200
    data = response.json()
    assert "sent" in data
    assert "received" in data
    assert isinstance(data["sent"], list)
    assert isinstance(data["received"], list)


@pytest.mark.asyncio
async def test_get_conversation(client_with_db, seed_data):
    """GET /messages/conversation/{u1}/{u2} — переписка."""
    await client_with_db.post(
        "/messages/send/admin/bob",
        json={"content": "Conv 1"},
    )
    await client_with_db.post(
        "/messages/send/bob/admin",
        json={"content": "Conv 2"},
    )
    response = await client_with_db.get("/messages/conversation/admin/bob")
    assert response.status_code == 200
    data = response.json()
    assert len(data) >= 2


@pytest.mark.asyncio
async def test_get_message_by_id(client_with_db, seed_data):
    """GET /messages/{id} — сообщение по ID."""
    r = await client_with_db.post(
        "/messages/send/admin/bob",
        json={"content": "ById"},
    )
    msg_id = r.json()["id"]
    response = await client_with_db.get(f"/messages/{msg_id}")
    assert response.status_code == 200
    assert response.json()["content"] == "ById"
    assert response.json()["id"] == msg_id


@pytest.mark.asyncio
async def test_get_message_not_found(client_with_db):
    """GET /messages/99999 — не найдено."""
    response = await client_with_db.get("/messages/99999")
    assert response.status_code == 404


@pytest.mark.asyncio
async def test_mark_as_delivered(client_with_db, seed_data):
    """PATCH /messages/{id}/delivered — пометить доставленным."""
    r = await client_with_db.post(
        "/messages/send/admin/bob",
        json={"content": "Delivered"},
    )
    msg_id = r.json()["id"]
    # Уже доставлено, но эндпоинт должен вернуть 200
    response = await client_with_db.patch(f"/messages/{msg_id}/delivered")
    assert response.status_code == 200
    assert response.json()["is_delivered"] is True


@pytest.mark.asyncio
async def test_mark_as_read(client_with_db, seed_data):
    """PATCH /messages/{id}/read/{username} — пометить прочитанным."""
    r = await client_with_db.post(
        "/messages/send/admin/bob",
        json={"content": "Read me"},
    )
    msg_id = r.json()["id"]
    response = await client_with_db.patch(f"/messages/{msg_id}/read/bob")
    assert response.status_code == 200
    assert response.json()["is_read"] is True
    assert response.json()["read_at"] is not None


@pytest.mark.asyncio
async def test_mark_as_read_wrong_user(client_with_db, seed_data):
    """PATCH /messages/{id}/read/{username} — не получатель, 404."""
    r = await client_with_db.post(
        "/messages/send/admin/bob",
        json={"content": "For Bob"},
    )
    msg_id = r.json()["id"]
    # alice не получатель
    response = await client_with_db.patch(f"/messages/{msg_id}/read/alice")
    assert response.status_code == 404


# ============ INTEGRATIONS (Webhook) ============

TELEGRAM_PAYLOAD = {
    "update_id": 1,
    "message": {
        "message_id": 1,
        "from": {"id": 111222333, "username": "tg_user", "is_bot": False},
        "chat": {"id": 111222333, "type": "private"},
        "date": 1234567890,
        "text": "Hello from Telegram",
    },
}


@pytest.mark.asyncio
async def test_telegram_webhook_ok(client_with_db):
    """POST /webhook/telegram — возвращает 200."""
    response = await client_with_db.post(
        "/webhook/telegram",
        json=TELEGRAM_PAYLOAD,
    )
    assert response.status_code == 200
    assert response.json() == {"status": "ok"}


@pytest.mark.asyncio
async def test_telegram_webhook_creates_user_and_message(client_with_db):
    """Webhook создаёт пользователя и сообщение в БД."""
    await client_with_db.post("/webhook/telegram", json=TELEGRAM_PAYLOAD)

    async with TestSessionLocal() as session:
        r = await session.execute(select(User).where(User.telegram_id == 111222333))
        user = r.scalars().first()
        assert user is not None
        assert user.username == "tg_user"

        r = await session.execute(select(Message).where(Message.source == MessageSource.TELEGRAM))
        msgs = r.scalars().all()
        assert len(msgs) == 1
        assert msgs[0].content == "Hello from Telegram"
