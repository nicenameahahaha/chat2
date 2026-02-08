package com.example.ihatemylife

import android.content.Context
import com.example.ihatemylife.repository.ContactRepository
import com.example.ihatemylife.repository.UserRepository

/**
 * Clears all added contacts and users from local storage (Room + DatabaseHelper).
 * Use for a full reset of contact/user data.
 */
object DataCleanup {

    /**
     * Clears all contacts and users:
     * - Room: contacts table, users table
     * - DatabaseHelper: user-created contacts (all users), in-memory registered users list
     */
    suspend fun clearAllContactsAndUsers(context: Context) {
        val contactRepository = ContactRepository(context)
        val userRepository = UserRepository(context)
        contactRepository.clearAllContacts()
        userRepository.clearAllUsers()
        DatabaseHelper.clearAllUserContacts()
        DatabaseHelper.clearAllUsers()
    }
}
