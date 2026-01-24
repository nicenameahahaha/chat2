package com.example.ihatemylife.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ihatemylife.database.dao.ChatDao
import com.example.ihatemylife.database.dao.ContactDao
import com.example.ihatemylife.database.dao.MessageDao
import com.example.ihatemylife.database.dao.UserDao
import com.example.ihatemylife.database.entities.ChatEntity
import com.example.ihatemylife.database.entities.ContactEntity
import com.example.ihatemylife.database.entities.MessageEntity
import com.example.ihatemylife.database.entities.UserEntity

/**
 * Room database for local data persistence
 * Offline-first approach: cache API data locally
 */
@Database(
    entities = [
        UserEntity::class,
        MessageEntity::class,
        ChatEntity::class,
        ContactEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao
    abstract fun contactDao(): ContactDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "messenger_database"
                )
                    .fallbackToDestructiveMigration() // For development - remove in production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

