package com.example.ihatemylife.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ihatemylife.database.entities.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE id = :id")
    suspend fun getChatById(id: String): ChatEntity?
    
    @Query("SELECT * FROM chats WHERE id = :id")
    fun getChatByIdFlow(id: String): Flow<ChatEntity?>
    
    @Query("SELECT * FROM chats WHERE isActive = 1 ORDER BY lastMessageTimestamp DESC")
    fun getAllActiveChats(): Flow<List<ChatEntity>>
    
    @Query("SELECT * FROM chats WHERE isActive = 1 ORDER BY lastMessageTimestamp DESC")
    suspend fun getAllActiveChatsList(): List<ChatEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<ChatEntity>)
    
    @Update
    suspend fun updateChat(chat: ChatEntity)
    
    @Query("UPDATE chats SET lastMessage = :message, lastMessageTimestamp = :timestamp WHERE id = :chatId")
    suspend fun updateLastMessage(chatId: String, message: String, timestamp: Long)
    
    @Query("UPDATE chats SET isMuted = :isMuted WHERE id = :chatId")
    suspend fun setMuted(chatId: String, isMuted: Boolean)
    
    @Query("UPDATE chats SET isActive = :isActive WHERE id = :chatId")
    suspend fun setActive(chatId: String, isActive: Boolean)
    
    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun deleteChat(id: String)

    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()
}

