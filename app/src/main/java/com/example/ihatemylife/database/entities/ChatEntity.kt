package com.example.ihatemylife.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for Chat (conversation between two users)
 * Backend doesn't have separate Chat model, but we need it for UI
 * Represents a conversation between current user and another user
 */
@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val id: String, // Format: "chat_{userId1}_{userId2}" or "chat_{userId}" for group
    val title: String,
    val lastMessage: String? = null,
    val lastMessageTimestamp: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val isGroup: Boolean = false,
    val participantIds: String = "", // Comma-separated user IDs
    val isMuted: Boolean = false,
    val syncedAt: Long = System.currentTimeMillis()
)

