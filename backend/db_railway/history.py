"""
Chat history from Railway PostgreSQL (messages + sender/receiver usernames).
"""
from db_railway.connection import get_connection


async def get_chat_history(user_id: int):
    """Return list of rows: sender, receiver, message, sent_at for the given user_id."""
    conn = await get_connection()
    try:
        rows = await conn.fetch(
            """
            SELECT u1.username AS sender, u2.username AS receiver,
                   m.message, m.sent_at
            FROM messages m
            JOIN users u1 ON m.sender_id = u1.id
            JOIN users u2 ON m.receiver_id = u2.id
            WHERE m.receiver_id = $1 OR m.sender_id = $1
            ORDER BY m.sent_at
            """,
            user_id,
        )
        return rows
    finally:
        await conn.close()
