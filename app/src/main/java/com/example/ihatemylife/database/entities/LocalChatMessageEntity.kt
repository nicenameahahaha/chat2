package com.example.ihatemylife.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for messages in local-only chats (e.g. created contacts with no backend user).
 * Keyed by chatId; messages are not synced to backend.
 */
@Entity(
    tableName = "local_chat_messages",
    indices = [Index(value = ["chatId"]), Index(value = ["timestamp"])]
)
data class LocalChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val chatId: String,
    val senderId: Int,
    val content: String,
    val timestamp: Long,
    val replyToMessageId: Int? = null
)
