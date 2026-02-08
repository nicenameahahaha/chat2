"""
Register user in Railway PostgreSQL (users: username, password, email).
Uses Argon2 for password hashing.
"""
from argon2 import PasswordHasher

from db_railway.connection import get_connection

ph = PasswordHasher()


async def register_user(username: str, password: str, email: str) -> None:
    conn = await get_connection()
    try:
        password_hash = ph.hash(password)
        await conn.execute(
            """
            INSERT INTO users (username, password, email)
            VALUES ($1, $2, $3)
            """,
            username,
            password_hash,
            email,
        )
    finally:
        await conn.close()
