package com.example.ihatemylife.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for Message
 * Maps to backend Message model
 * Uses sender_id and receiver_id (not chat_id)
 */
@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["receiverId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["senderId"]),
        Index(value = ["receiverId"]),
        Index(value = ["timestamp"])
    ]
)
data class MessageEntity(
    @PrimaryKey
    val id: Int,
    val senderId: Int,
    val receiverId: Int? = null,
    val content: String,
    val timestamp: Long, // Stored as epoch milliseconds
    val isDelivered: Boolean = false,
    val isRead: Boolean = false,
    val readAt: Long? = null,
    val source: String = "own_messenger", // "own_messenger" or "telegram"
    val replyToMessageId: Int? = null, // For reply system (not in backend yet, but prepared)
    val syncedAt: Long = System.currentTimeMillis()
)

