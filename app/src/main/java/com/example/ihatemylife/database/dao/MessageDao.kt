package com.example.ihatemylife.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ihatemylife.database.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getMessageById(id: Int): MessageEntity?
    
    @Query("SELECT * FROM messages WHERE senderId = :userId OR receiverId = :userId ORDER BY timestamp ASC")
    fun getAllMessagesForUser(userId: Int): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE (senderId = :userId1 AND receiverId = :userId2) OR (senderId = :userId2 AND receiverId = :userId1) ORDER BY timestamp ASC")
    fun getConversation(userId1: Int, userId2: Int): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE senderId = :userId ORDER BY timestamp DESC")
    fun getSentMessages(userId: Int): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE receiverId = :userId ORDER BY timestamp DESC")
    fun getReceivedMessages(userId: Int): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE (senderId = :userId OR receiverId = :userId) AND timestamp >= :fromTimestamp ORDER BY timestamp ASC")
    fun getMessagesAfter(userId: Int, fromTimestamp: Long): Flow<List<MessageEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)
    
    @Update
    suspend fun updateMessage(message: MessageEntity)
    
    @Query("UPDATE messages SET isRead = 1, readAt = :readAt WHERE id = :messageId")
    suspend fun markAsRead(messageId: Int, readAt: Long)
    
    @Query("UPDATE messages SET isDelivered = 1 WHERE id = :messageId")
    suspend fun markAsDelivered(messageId: Int)
    
    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessage(id: Int)
    
    @Query("SELECT * FROM messages WHERE replyToMessageId = :messageId")
    suspend fun getRepliesToMessage(messageId: Int): List<MessageEntity>

    /**
     * Last activity timestamp for presence (last seen).
     * Returns the latest message timestamp where the user is sender or receiver.
     */
    @Query("SELECT MAX(timestamp) FROM messages WHERE senderId = :userId OR receiverId = :userId")
    suspend fun getLastActivityTimestampForUser(userId: Int): Long?

    /**
     * Distinct user IDs that appear in messages (senders and receivers).
     * Used to discover users from synced messages and insert placeholders into users table.
     */
    @Query("SELECT DISTINCT senderId FROM messages UNION SELECT DISTINCT receiverId FROM messages WHERE receiverId IS NOT NULL")
    suspend fun getDistinctUserIdsFromMessages(): List<Int>
}

