import asyncpg


async def get_chat_history(user_id):
    conn = await asyncpg.connect('postgresql://postgres:25MySwitch09_Owa@20@localhost/messenger')

    rows = await conn.fetch("""
        SELECT u1.username AS sender, u2.username AS receiver, 
               m.message, m.sent_at
        FROM messages m
        JOIN users u1 ON m.sender_id = u1.id
        JOIN users u2 ON m.receiver_id = u2.id
        WHERE m.receiver_id = $1 OR m.sender_id = $1
        ORDER BY m.sent_at
    """, user_id)

    await conn.close()
    return rows
