"""
Railway PostgreSQL layer (integrated from «База данных»).
Uses asyncpg + Argon2. Set RAILWAY_DATABASE_URL in .env to enable.
"""
from db_railway.connection import get_connection
from db_railway.register_user import register_user
from db_railway.find_user import login_user
from db_railway.history import get_chat_history
from db_railway.messages import send_message

__all__ = [
    "get_connection",
    "register_user",
    "login_user",
    "get_chat_history",
    "send_message",
]
