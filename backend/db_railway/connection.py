"""
Connection helper for Railway PostgreSQL (db_railway).
Uses RAILWAY_DATABASE_URL from env; no hardcoded credentials.
"""
import asyncpg

from core.config import settings


async def get_connection():
    """Return asyncpg connection. Requires RAILWAY_DATABASE_URL to be set (postgresql://...)."""
    url = settings.RAILWAY_DATABASE_URL
    if not url:
        raise ValueError(
            "RAILWAY_DATABASE_URL is not set. Add it to .env to use the Railway PostgreSQL layer."
        )
    return await asyncpg.connect(url)
