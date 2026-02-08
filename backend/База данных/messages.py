import asyncpg


async def send_message(sender_id, receiver_username, message_text):
    conn = await asyncpg.connect('postgresql://postgres:25MySwitch09_Owa@20@localhost/messenger')

    # Получаем ID получателя
    receiver_id = await conn.fetchval("""
        SELECT id FROM users WHERE username = $1
    """, receiver_username)

    if receiver_id:
        await conn.execute("""
            INSERT INTO messages (sender_id, receiver_id, message)
            VALUES ($1, $2, $3)
        """, sender_id, receiver_id, message_text)

    await conn.close()
