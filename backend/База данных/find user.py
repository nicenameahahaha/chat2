import asyncpg
from argon2 import PasswordHasher

ph = PasswordHasher()


async def login_user(username, password):
    conn = await asyncpg.connect('postgresql://postgres:bWxwEtCVffrEnGOwKgertZtLesXuXVbb@tramway.proxy.rlwy.net:15565/railway')

    user = await conn.fetchrow("""
        SELECT id, username, password FROM users WHERE username = $1
    """, username)

    if user and ph.verify(user['password'], password):
        print(f"Пользователь {username} авторизован, ID: {user['id']}")
        await conn.close()
        return user['id']

    await conn.close()
    return None
