"""
Менеджер WebSocket-соединений. Вынесен в core, чтобы его могли использовать
и api/websocket, и services (например, рассылка при входящих сообщениях из Telegram).
"""
from typing import Dict
from fastapi import WebSocket

connections: Dict[str, WebSocket] = {}


class ConnectionManager:
    """Менеджер WebSocket соединений."""

    async def connect(self, websocket: WebSocket, username: str):
        await websocket.accept()
        connections[username] = websocket

    def disconnect(self, username: str):
        if username in connections:
            del connections[username]

    async def send_personal_message(self, message: dict, username: str):
        """Отправить сообщение конкретному пользователю."""
        if username in connections:
            try:
                await connections[username].send_json(message)
            except Exception:
                self.disconnect(username)

    async def broadcast(self, message: dict, exclude_username: str = None):
        """Отправить сообщение всем подключённым (например, при новом сообщении из Telegram)."""
        disconnected = []
        for username, connection in list(connections.items()):
            if username != exclude_username:
                try:
                    await connection.send_json(message)
                except Exception:
                    disconnected.append(username)
        for username in disconnected:
            self.disconnect(username)


manager = ConnectionManager()
