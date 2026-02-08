package com.example.ihatemylife.repository

import android.content.Context
import com.example.ihatemylife.database.AppDatabase
import com.example.ihatemylife.database.dao.ContactDao
import com.example.ihatemylife.database.entities.ContactEntity
import com.example.ihatemylife.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for contact operations.
 * Contacts are stored locally only (current backend has no contacts API).
 */
class ContactRepository(context: Context) {
    private val contactDao: ContactDao = AppDatabase.getDatabase(context).contactDao()
    
    /**
     * Convert entity to domain Contact
     */
    private fun entityToDomain(entity: ContactEntity): Contact {
        return Contact(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            phone = entity.phone,
            username = entity.username
        )
    }
    
    /**
     * Convert domain Contact to entity
     */
    private fun domainToEntity(contact: Contact, userId: String): ContactEntity {
        return ContactEntity(
            id = contact.id,
            userId = userId,
            firstName = contact.firstName,
            lastName = contact.lastName,
            email = contact.email,
            phone = contact.phone,
            username = contact.username
        )
    }
    
    /**
     * Get all contacts for a user
     */
    fun getContactsForUser(userId: String): Flow<List<Contact>> {
        return contactDao.getContactsForUserFlow(userId)
            .map { entities -> entities.map { entityToDomain(it) } }
    }
    
    /**
     * Add contact for a user
     */
    suspend fun addContact(contact: Contact, userId: String, backendUserId: Int? = null): Result<Contact> {
        return try {
            // Check for duplicates
            val existing = contactDao.findContactByEmailOrPhone(
                userId,
                contact.email,
                contact.phone
            )
            if (existing != null) {
                val duplicateField = if (contact.email != null && existing.email == contact.email) {
                    "email"
                } else {
                    "phone number"
                }
                return Result.failure(Exception("Contact with this $duplicateField already exists"))
            }
            
            // Save locally first
            contactDao.insertContact(domainToEntity(contact, userId))
            
            // Try to sync to backend if userId is available
            if (backendUserId != null) {
                syncContactToBackend(contact, backendUserId).onFailure {
                    // If backend sync fails, contact is still saved locally
                    // Log error but don't fail the operation
                }
            }
            
            Result.success(contact)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Sync contact to backend.
     * Current backend (users + messages only) has no contacts API; contacts are stored locally only.
     */
    suspend fun syncContactToBackend(contact: Contact, userId: Int): Result<Unit> {
        return Result.success(Unit)
    }
    
    /**
     * Delete contact
     */
    suspend fun deleteContact(contactId: String, userId: String) {
        contactDao.deleteContact(contactId, userId)
    }
    
    /**
     * Clear all contacts for a user
     */
    suspend fun clearContactsForUser(userId: String) {
        contactDao.deleteAllContactsForUser(userId)
    }

    /**
     * Delete all contacts from the local database (all users).
     */
    suspend fun clearAllContacts() {
        contactDao.deleteAllContacts()
    }
}

