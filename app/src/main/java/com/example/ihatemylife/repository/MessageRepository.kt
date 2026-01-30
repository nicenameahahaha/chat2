package com.example.ihatemylife.repository

import android.content.Context
import com.example.ihatemylife.api.ApiClient
import com.example.ihatemylife.api.models.ApiMessageCreate
import com.example.ihatemylife.api.models.ApiMessageOut
import com.example.ihatemylife.database.AppDatabase
import com.example.ihatemylife.database.dao.MessageDao
import com.example.ihatemylife.database.dao.UserDao
import com.example.ihatemylife.database.entities.MessageEntity
import com.example.ihatemylife.Message
import com.example.ihatemylife.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Repository for message operations
 * Handles sending/receiving messages and syncing with backend
 */
class MessageRepository(context: Context) {
    private val apiService = ApiClient.getApiService(context)
    private val messageDao: MessageDao = AppDatabase.getDatabase(context).messageDao()
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    
    /**
     * Convert API message to domain Message
     */
    private fun apiMessageToDomain(apiMessage: ApiMessageOut): Message {
        val timestamp = parseTimestamp(apiMessage.timestamp)
        return Message(
            id = apiMessage.id,
            senderId = apiMessage.senderId,
            receiverId = apiMessage.receiverId,
            content = apiMessage.content,
            timestamp = timestamp,
            isDelivered = apiMessage.isDelivered,
            isRead = apiMessage.isRead,
            readAt = apiMessage.readAt?.let { parseTimestamp(it) },
            source = apiMessage.source,
            replyToMessageId = apiMessage.replyToMessageId
        )
    }
    
    /**
     * Convert API message to entity
     */
    private fun apiMessageToEntity(apiMessage: ApiMessageOut): MessageEntity {
        val timestamp = parseTimestamp(apiMessage.timestamp)
        return MessageEntity(
            id = apiMessage.id,
            senderId = apiMessage.senderId,
            receiverId = apiMessage.receiverId,
            content = apiMessage.content,
            timestamp = timestamp,
            isDelivered = apiMessage.isDelivered,
            isRead = apiMessage.isRead,
            readAt = apiMessage.readAt?.let { parseTimestamp(it) },
            source = apiMessage.source,
            replyToMessageId = apiMessage.replyToMessageId
        )
    }
    
    /**
     * Convert entity to domain Message
     */
    private fun entityToDomain(entity: MessageEntity): Message {
        return Message(
            id = entity.id,
            senderId = entity.senderId,
            receiverId = entity.receiverId,
            content = entity.content,
            timestamp = entity.timestamp,
            isDelivered = entity.isDelivered,
            isRead = entity.isRead,
            readAt = entity.readAt,
            source = entity.source,
            replyToMessageId = entity.replyToMessageId
        )
    }
    
    /**
     * Parse ISO datetime string to epoch milliseconds
     */
    private fun parseTimestamp(timestamp: String): Long {
        return try {
            dateFormat.parse(timestamp)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
    
    /**
     * Send message via API
     */
    suspend fun sendMessage(
        senderUsername: String,
        receiverUsername: String,
        content: String,
        replyToMessageId: Int? = null
    ): Result<Message> {
        return try {
            val response = apiService.sendMessageToUser(
                senderUsername,
                receiverUsername,
                ApiMessageCreate(content, replyToMessageId = replyToMessageId)
            )
            if (response.isSuccessful && response.body() != null) {
                val apiMessage = response.body()!!
                val message = apiMessageToDomain(apiMessage)
                // Save to local database
                messageDao.insertMessage(apiMessageToEntity(apiMessage))
                Result.success(message)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to send message"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get conversation between two users
     */
    fun getConversation(userId1: Int, userId2: Int): Flow<List<Message>> {
        return messageDao.getConversation(userId1, userId2)
            .map { entities -> entities.map { entityToDomain(it) } }
    }
    
    /**
     * Get all messages for a user
     */
    fun getAllMessagesForUser(userId: Int): Flow<List<Message>> {
        return messageDao.getAllMessagesForUser(userId)
            .map { entities -> entities.map { entityToDomain(it) } }
    }

    /**
     * Get messages from integrated messengers (e.g. Telegram) for display in chat history.
     * Placeholder: returns empty list until Telegram integration is implemented.
     */
    fun getTelegramMessagesForUser(userId: Int): Flow<List<Message>> = flowOf(emptyList())
    
    /**
     * Sync messages from API for a user.
     * After inserting messages, discovers sender/receiver IDs and ensures they exist in the users table
     * (inserts placeholder UserEntity(id, "User$id") so pseudo-global user list includes message peers).
     */
    suspend fun syncMessages(username: String): Result<Unit> {
        return try {
            // Get all messages (sent + received)
            val response = apiService.getAllMessages(username)
            if (response.isSuccessful && response.body() != null) {
                val allMessages = response.body()!!
                val messages = allMessages.sent + allMessages.received

                // Save to local database
                val entities = messages.map { apiMessageToEntity(it) }
                messageDao.insertMessages(entities)

                // Discover users from messages: ensure every sender/receiver exists in users table
                ensureUsersFromMessages()
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to sync messages"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Ensures all user IDs that appear in messages exist in the users table.
     * Inserts placeholder UserEntity(id, "User$id") for unknown IDs so the pseudo-global list grows from sync.
     */
    private suspend fun ensureUsersFromMessages() {
        val ids = messageDao.getDistinctUserIdsFromMessages()
        val existing = userDao.getAllUsers().map { it.id }.toSet()
        ids.filter { it !in existing }.forEach { id ->
            userDao.insertUser(UserEntity(id = id, username = "User$id"))
        }
    }
    
    /**
     * Mark message as read
     */
    suspend fun markAsRead(messageId: Int, username: String): Result<Unit> {
        return try {
            val response = apiService.markMessageAsRead(messageId, username)
            if (response.isSuccessful && response.body() != null) {
                val readAt = System.currentTimeMillis()
                messageDao.markAsRead(messageId, readAt)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to mark as read"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Last activity timestamp for a user (presence / last seen).
     * Returns the latest message timestamp where the user is sender or receiver.
     */
    suspend fun getLastActivityTimestampForUser(userId: Int): Long? {
        return messageDao.getLastActivityTimestampForUser(userId)
    }

    /**
     * Mark message as delivered
     */
    suspend fun markAsDelivered(messageId: Int): Result<Unit> {
        return try {
            val response = apiService.markMessageAsDelivered(messageId)
            if (response.isSuccessful) {
                messageDao.markAsDelivered(messageId)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to mark as delivered"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

