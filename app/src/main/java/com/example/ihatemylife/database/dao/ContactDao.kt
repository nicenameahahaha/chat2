package com.example.ihatemylife.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ihatemylife.database.entities.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts WHERE id = :id AND userId = :userId")
    suspend fun getContactById(id: String, userId: String): ContactEntity?
    
    @Query("SELECT * FROM contacts WHERE userId = :userId")
    suspend fun getContactsForUser(userId: String): List<ContactEntity>
    
    @Query("SELECT * FROM contacts WHERE userId = :userId")
    fun getContactsForUserFlow(userId: String): Flow<List<ContactEntity>>
    
    @Query("SELECT * FROM contacts WHERE userId = :userId AND (email = :email OR phone = :phone)")
    suspend fun findContactByEmailOrPhone(userId: String, email: String?, phone: String?): ContactEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)
    
    @Update
    suspend fun updateContact(contact: ContactEntity)
    
    @Query("DELETE FROM contacts WHERE id = :id AND userId = :userId")
    suspend fun deleteContact(id: String, userId: String)
    
    @Query("DELETE FROM contacts WHERE userId = :userId")
    suspend fun deleteAllContactsForUser(userId: String)

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()
}

