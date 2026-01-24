package com.example.ihatemylife.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for User
 * Maps to backend User model (id, username)
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val syncedAt: Long = System.currentTimeMillis()
)

