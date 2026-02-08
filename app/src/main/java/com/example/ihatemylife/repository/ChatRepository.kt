package com.example.ihatemylife.repository

import android.content.Context
import com.example.ihatemylife.database.AppDatabase
import com.example.ihatemylife.database.dao.ChatDao
import com.example.ihatemylife.database.dao.MessageDao
import com.example.ihatemylife.database.entities.ChatEntity
import com.example.ihatemylife.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for chat operations
 * Manages chat list and updates based on messages
 */
class ChatRepository(context: Context) {
    private val chatDao: ChatDao = AppDatabase.getDatabase(context).chatDao()
    private val messageDao: MessageDao = AppDatabase.getDatabase(context).messageDao()
    
    /**
     * Convert entity to domain Chat
     */
    private fun entityToDomain(entity: ChatEntity): Chat {
        val participantIds = if (entity.participantIds.isNotEmpty()) {
            entity.participantIds.split(",").filter { it.isNotEmpty() }
        } else {
            emptyList()
        }
        return Chat(
            id = entity.id,
            title = entity.title,
            lastMessage = entity.lastMessage ?: "",
            isActive = entity.isActive,
            lastUpdated = entity.lastMessageTimestamp,
            isGroup = entity.isGroup,
            participantIds = participantIds,
            isMuted = entity.isMuted
        )
    }
    
    /**
     * Convert domain Chat to entity
     */
    private fun domainToEntity(chat: Chat): ChatEntity {
        return ChatEntity(
            id = chat.id,
            title = chat.title,
            lastMessage = chat.lastMessage,
            lastMessageTimestamp = chat.lastUpdated,
            isActive = chat.isActive,
            isGroup = chat.isGroup,
            participantIds = chat.participantIds.joinToString(","),
            isMuted = chat.isMuted
        )
    }
    
    /**
     * Get all active chats
     */
    fun getAllActiveChats(): Flow<List<Chat>> {
        return chatDao.getAllActiveChats()
            .map { entities -> entities.map { entityToDomain(it) } }
    }
    
    /**
     * Get chat by ID
     */
    suspend fun getChatById(id: String): Chat? {
        return chatDao.getChatById(id)?.let { entityToDomain(it) }
    }
    
    /**
     * Create or update chat
     */
    suspend fun upsertChat(chat: Chat) {
        chatDao.insertChat(domainToEntity(chat))
    }
    
    /**
     * Update chat's last message
     */
    suspend fun updateLastMessage(chatId: String, message: String, timestamp: Long) {
        chatDao.updateLastMessage(chatId, message, timestamp)
    }
    
    /**
     * Set chat muted status
     */
    suspend fun setMuted(chatId: String, isMuted: Boolean) {
        chatDao.setMuted(chatId, isMuted)
    }
    
    /**
     * Set chat active status
     */
    suspend fun setActive(chatId: String, isActive: Boolean) {
        chatDao.setActive(chatId, isActive)
    }
    
    /**
     * Create chat ID for conversation between two users
     */
    fun createChatId(userId1: Int, userId2: Int): String {
        // Sort IDs to ensure consistent chat ID regardless of order
        val sortedIds = listOf(userId1, userId2).sorted()
        return "chat_${sortedIds[0]}_${sortedIds[1]}"
    }

    /**
     * Delete all chats from the local database.
     */
    suspend fun clearAllChats() {
        chatDao.deleteAllChats()
    }
}

