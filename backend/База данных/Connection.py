import asyncpg
from argon2 import PasswordHasher
import asyncio

ph = PasswordHasher()


async def register_user(username, password, email):
    conn = await asyncpg.connect('postgresql://postgres:25MySwitch09_Owa@20@localhost/messenger')

    # Хэшируем пароль
    password_hash = ph.hash(password)

    await conn.execute("""
        INSERT INTO users (username, password, email) 
        VALUES ($1, $2, $3)
    """, username, password_hash, email)

    await conn.close()
