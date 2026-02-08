"""
Send message in Railway PostgreSQL (messages: sender_id, receiver_id, message).
"""
from db_railway.connection import get_connection


async def send_message(sender_id: int, receiver_username: str, message_text: str) -> None:
    conn = await get_connection()
    try:
        receiver_id = await conn.fetchval(
            """
            SELECT id FROM users WHERE username = $1
            """,
            receiver_username,
        )
        if receiver_id:
            await conn.execute(
                """
                INSERT INTO messages (sender_id, receiver_id, message)
                VALUES ($1, $2, $3)
                """,
                sender_id,
                receiver_id,
                message_text,
            )
    finally:
        await conn.close()
