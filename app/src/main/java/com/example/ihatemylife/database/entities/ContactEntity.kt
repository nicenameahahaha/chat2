package com.example.ihatemylife.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for Contact
 * User-created contacts (not system users)
 */
@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val id: String,
    val userId: String, // Owner of this contact
    val firstName: String,
    val lastName: String = "",
    val email: String? = null,
    val phone: String? = null,
    val username: String? = null, // If contact is a registered user
    val syncedAt: Long = System.currentTimeMillis()
)

