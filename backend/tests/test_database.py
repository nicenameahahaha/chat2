"""
Тесты базы данных: модели, сессии, CRUD-операции.
Проверяет работу SQLAlchemy с user_service и message_service.
"""
import pytest
import pytest_asyncio
from unittest.mock import AsyncMock, patch
from sqlalchemy import select, text

from db.session import Base, get_db
from models.user import User
from models.message import Message, MessageSource
from models.integration import Integration
from schemas.user import UserCreate
from services.user_service import (
    create_user,
    get_user_by_username,
    get_user_by_telegram_id,
    create_telegram_user,
    link_telegram_to_user,
)
from services.message_service import (
    create_message,
    create_own_messenger_message,
    get_messages_sent,
    get_messages_received,
    get_all_user_messages,
    get_message_by_id,
    mark_message_as_delivered,
    mark_message_as_read,
    get_conversation,
)
from tests.conftest import test_engine, TestSessionLocal


@pytest_asyncio.fixture
async def db_session():
    """Создать таблицы и сессию для каждого теста."""
    async with test_engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)

    async with TestSessionLocal() as session:
        yield session

    async with test_engine.begin() as conn:
        await conn.run_sync(Base.metadata.drop_all)


# ============ Создание таблиц ============

@pytest.mark.asyncio
async def test_tables_created(db_session):
    """Таблицы users, messages, integration созданы."""
    result = await db_session.execute(
        text(
            "SELECT name FROM sqlite_master WHERE type='table' "
            "AND name IN ('users', 'messages', 'integration') ORDER BY name"
        )
    )
    tables = [row[0] for row in result.fetchall()]
    assert "users" in tables
    assert "messages" in tables
    assert "integration" in tables


# ============ User ============

@pytest.mark.asyncio
async def test_create_user(db_session):
    """Создание пользователя через user_service."""
    user_in = UserCreate(username="alice", password="secret123")
    user = await create_user(db_session, user_in)
    assert user.id is not None
    assert user.username == "alice"
    assert user.hashed_password is not None
    assert user.hashed_password != "secret123"
    assert user.telegram_id is None


@pytest.mark.asyncio
async def test_get_user_by_username(db_session):
    """Получение пользователя по username."""
    user_in = UserCreate(username="bob", password="pass")
    created = await create_user(db_session, user_in)
    found = await get_user_by_username(db_session, "bob")
    assert found is not None
    assert found.id == created.id
    assert found.username == "bob"


@pytest.mark.asyncio
async def test_get_user_by_username_not_found(db_session):
    """Пользователь не найден — возвращается None."""
    found = await get_user_by_username(db_session, "nonexistent")
    assert found is None


@pytest.mark.asyncio
async def test_create_telegram_user(db_session):
    """Создание пользователя из Telegram."""
    user = await create_telegram_user(db_session, telegram_id=123456789, username="tg_user")
    assert user.id is not None
    assert user.telegram_id == 123456789
    assert user.username == "tg_user"
    assert user.hashed_password is None


@pytest.mark.asyncio
async def test_create_telegram_user_default_username(db_session):
    """Telegram-пользователь без username получает tg_{id}."""
    user = await create_telegram_user(db_session, telegram_id=999, username=None)
    assert user.username == "tg_999"


@pytest.mark.asyncio
async def test_get_user_by_telegram_id(db_session):
    """Получение пользователя по telegram_id."""
    created = await create_telegram_user(db_session, 555, "telegram_user")
    found = await get_user_by_telegram_id(db_session, 555)
    assert found is not None
    assert found.id == created.id
    assert found.telegram_id == 555


@pytest.mark.asyncio
async def test_link_telegram_to_user(db_session):
    """Привязка Telegram к существующему пользователю."""
    user_in = UserCreate(username="alice", password="p")
    user = await create_user(db_session, user_in)
    assert user.telegram_id is None

    linked = await link_telegram_to_user(db_session, "alice", 777888999)
    assert linked is not None
    assert linked.telegram_id == 777888999
    assert linked.username == "alice"


@pytest.mark.asyncio
async def test_link_telegram_user_not_found(db_session):
    """Привязка к несуществующему пользователю — None."""
    result = await link_telegram_to_user(db_session, "nobody", 111)
    assert result is None


@pytest.mark.asyncio
async def test_link_telegram_unlinks_previous_owner(db_session):
    """При привязке telegram_id к новому юзеру — у старого снимается."""
    u1 = await create_user(db_session, UserCreate(username="u1", password="p"))
    u2 = await create_telegram_user(db_session, telegram_id=100, username="u2")

    linked = await link_telegram_to_user(db_session, "u1", 100)
    assert linked.telegram_id == 100

    # u2 больше не имеет этого telegram_id
    u2_refresh = await get_user_by_username(db_session, "u2")
    assert u2_refresh.telegram_id is None


# ============ Message ============

@pytest.mark.asyncio
async def test_create_own_messenger_message(db_session):
    """Создание сообщения в собственном мессенджере."""
    sender = await create_user(db_session, UserCreate(username="alice", password="p"))
    receiver = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        msg = await create_own_messenger_message(
            db_session, sender=sender, receiver=receiver, content="Hello Bob!"
        )

    assert msg.id is not None
    assert msg.sender_id == sender.id
    assert msg.receiver_id == receiver.id
    assert msg.content == "Hello Bob!"
    assert msg.source == MessageSource.OWN_MESSENGER
    assert msg.is_delivered is True
    assert msg.is_read is False
    assert msg.read_at is None


@pytest.mark.asyncio
async def test_create_message_telegram_source(db_session):
    """Создание сообщения из Telegram (без получателя)."""
    sender = await create_telegram_user(db_session, 111, "tg_sender")

    msg = await create_message(
        db_session,
        sender=sender,
        content="From Telegram",
        receiver=None,
        source=MessageSource.TELEGRAM,
    )

    assert msg.receiver_id is None
    assert msg.source == MessageSource.TELEGRAM
    assert msg.is_delivered is False


@pytest.mark.asyncio
async def test_get_messages_sent(db_session):
    """Получение отправленных сообщений."""
    alice = await create_user(db_session, UserCreate(username="alice", password="p"))
    bob = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        await create_own_messenger_message(db_session, alice, bob, "Msg 1")
        await create_own_messenger_message(db_session, alice, bob, "Msg 2")

    sent = await get_messages_sent(db_session, alice)
    assert len(sent) == 2
    assert all(m.sender_id == alice.id for m in sent)


@pytest.mark.asyncio
async def test_get_messages_received(db_session):
    """Получение полученных сообщений."""
    alice = await create_user(db_session, UserCreate(username="alice", password="p"))
    bob = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        await create_own_messenger_message(db_session, alice, bob, "To Bob")

    received = await get_messages_received(db_session, bob)
    assert len(received) == 1
    assert received[0].receiver_id == bob.id
    assert received[0].content == "To Bob"


@pytest.mark.asyncio
async def test_get_all_user_messages(db_session):
    """Получение всех сообщений пользователя (sent + received)."""
    alice = await create_user(db_session, UserCreate(username="alice", password="p"))
    bob = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        await create_own_messenger_message(db_session, alice, bob, "A to B")
        await create_own_messenger_message(db_session, bob, alice, "B to A")

    data = await get_all_user_messages(db_session, alice)
    assert "sent" in data
    assert "received" in data
    assert len(data["sent"]) == 1
    assert len(data["received"]) == 1


@pytest.mark.asyncio
async def test_get_message_by_id(db_session):
    """Получение сообщения по ID."""
    alice = await create_user(db_session, UserCreate(username="alice", password="p"))
    bob = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        msg = await create_own_messenger_message(db_session, alice, bob, "ById")

    found = await get_message_by_id(db_session, msg.id)
    assert found is not None
    assert found.id == msg.id
    assert found.content == "ById"


@pytest.mark.asyncio
async def test_get_message_by_id_not_found(db_session):
    """Сообщение не найдено — None."""
    found = await get_message_by_id(db_session, 99999)
    assert found is None


@pytest.mark.asyncio
async def test_mark_message_as_delivered(db_session):
    """Пометка сообщения как доставленного."""
    alice = await create_user(db_session, UserCreate(username="alice", password="p"))
    bob = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        msg = await create_message(
            db_session, sender=alice, receiver=bob, content="Deliver",
            source=MessageSource.OWN_MESSENGER
        )
    # create_message с receiver уже ставит is_delivered=True, но проверим эндпоинт
    updated = await mark_message_as_delivered(db_session, msg.id)
    assert updated.is_delivered is True


@pytest.mark.asyncio
async def test_mark_message_as_read(db_session):
    """Пометка сообщения как прочитанного получателем."""
    alice = await create_user(db_session, UserCreate(username="alice", password="p"))
    bob = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        msg = await create_own_messenger_message(db_session, alice, bob, "Read me")

    updated = await mark_message_as_read(db_session, msg.id, bob)
    assert updated is not None
    assert updated.is_read is True
    assert updated.read_at is not None


@pytest.mark.asyncio
async def test_mark_message_as_read_wrong_user(db_session):
    """Попытка пометить прочитанным не получателем — None."""
    alice = await create_user(db_session, UserCreate(username="alice", password="p"))
    bob = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        msg = await create_own_messenger_message(db_session, alice, bob, "For Bob")

    # alice не получатель
    result = await mark_message_as_read(db_session, msg.id, alice)
    assert result is None


@pytest.mark.asyncio
async def test_get_conversation(db_session):
    """Получение переписки между двумя пользователями."""
    alice = await create_user(db_session, UserCreate(username="alice", password="p"))
    bob = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        await create_own_messenger_message(db_session, alice, bob, "A→B 1")
        await create_own_messenger_message(db_session, bob, alice, "B→A 1")
        await create_own_messenger_message(db_session, alice, bob, "A→B 2")

    conv = await get_conversation(db_session, alice.id, bob.id)
    assert len(conv) == 3
    assert conv[0].content == "A→B 1"
    assert conv[1].content == "B→A 1"
    assert conv[2].content == "A→B 2"


@pytest.mark.asyncio
async def test_get_conversation_excludes_telegram(db_session):
    """Переписка не включает сообщения из Telegram."""
    alice = await create_user(db_session, UserCreate(username="alice", password="p"))
    bob = await create_user(db_session, UserCreate(username="bob", password="p"))

    with patch("services.message_service.send_telegram_message", new_callable=AsyncMock):
        await create_own_messenger_message(db_session, alice, bob, "Own")
    await create_message(
        db_session, sender=alice, receiver=bob, content="TG",
        source=MessageSource.TELEGRAM
    )

    conv = await get_conversation(db_session, alice.id, bob.id)
    assert len(conv) == 1
    assert conv[0].content == "Own"
