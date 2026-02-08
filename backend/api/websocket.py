from fastapi import APIRouter, WebSocket, WebSocketDisconnect
from services.message_service import create_own_messenger_message
from services.user_service import get_user_by_username
from db.session import AsyncSessionLocal
from core.ws_manager import manager

router = APIRouter()


@router.websocket("/ws/chat/{username}")
async def websocket_chat(websocket: WebSocket, username: str):
    """
    WebSocket для собственного мессенджера.
    
    Формат сообщений:
    - От клиента: {"type": "message", "receiver": "username", "content": "текст"}
    - От сервера: {"type": "message", "sender": "username", "content": "текст", "message_id": 1, "timestamp": "...", "is_delivered": true}
    """
    async with AsyncSessionLocal() as db:
        user = await get_user_by_username(db, username)
        if not user:
            await websocket.close(code=1008, reason="User not found")
            return
        
        await manager.connect(websocket, username)
        
        try:
            while True:
                data = await websocket.receive_json()
                
                if data.get("type") == "message":
                    receiver_username = data.get("receiver")
                    content = data.get("content")
                    
                    if not receiver_username or not content:
                        await websocket.send_json({
                            "type": "error",
                            "message": "receiver and content are required"
                        })
                        continue
                    
                    # Получаем получателя
                    receiver = await get_user_by_username(db, receiver_username)
                    if not receiver:
                        await websocket.send_json({
                            "type": "error",
                            "message": f"Receiver '{receiver_username}' not found"
                        })
                        continue
                    
                    # Создаем сообщение в собственном мессенджере
                    message = await create_own_messenger_message(
                        db,
                        sender=user,
                        receiver=receiver,
                        content=content,
                    )
                    
                    # Формируем ответ для отправки
                    message_data = {
                        "type": "message",
                        "message_id": message.id,
                        "sender": user.username,
                        "receiver": receiver.username,
                        "content": message.content,
                        "timestamp": message.timestamp.isoformat(),
                        "is_delivered": message.is_delivered,
                        "is_read": message.is_read,
                        "source": message.source.value,
                    }
                    
                    # Отправляем отправителю подтверждение
                    await manager.send_personal_message({
                        **message_data,
                        "status": "sent"
                    }, username)
                    
                    # Отправляем получателю сообщение
                    await manager.send_personal_message({
                        **message_data,
                        "status": "received"
                    }, receiver_username)
                
                elif data.get("type") == "mark_read":
                    # Пометить сообщение как прочитанное
                    message_id = data.get("message_id")
                    if message_id:
                        from services.message_service import mark_message_as_read
                        message = await mark_message_as_read(db, message_id, user)
                        if message:
                            # Уведомляем отправителя о прочтении
                            sender_username = None
                            from sqlalchemy import select
                            from models.message import Message
                            result = await db.execute(
                                select(Message).where(Message.id == message_id)
                            )
                            msg = result.scalars().first()
                            if msg and msg.sender:
                                sender_username = msg.sender.username
                            
                            if sender_username:
                                await manager.send_personal_message({
                                    "type": "read_receipt",
                                    "message_id": message_id,
                                    "read_by": username,
                                    "read_at": message.read_at.isoformat() if message.read_at else None,
                                }, sender_username)
        
        except WebSocketDisconnect:
            manager.disconnect(username)
        except Exception as e:
            manager.disconnect(username)
            # Логируем ошибку (в продакшене лучше использовать logger)
            print(f"WebSocket error for {username}: {e}")