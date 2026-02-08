package com.example.ihatemylife.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ihatemylife.database.entities.LocalChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalChatMessageDao {
    @Query("SELECT * FROM local_chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesByChatId(chatId: String): Flow<List<LocalChatMessageEntity>>

    @Insert
    suspend fun insert(message: LocalChatMessageEntity): Long
}
