"""
Login user against Railway PostgreSQL (Argon2 verification).
Returns user id if valid, None otherwise.
"""
from argon2 import PasswordHasher

from db_railway.connection import get_connection

ph = PasswordHasher()


async def login_user(username: str, password: str) -> int | None:
    conn = await get_connection()
    try:
        user = await conn.fetchrow(
            """
            SELECT id, username, password FROM users WHERE username = $1
            """,
            username,
        )
        if user and ph.verify(user["password"], password):
            return user["id"]
        return None
    finally:
        await conn.close()
