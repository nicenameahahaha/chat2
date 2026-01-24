package com.example.ihatemylife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihatemylife.Contact
import com.example.ihatemylife.repository.ContactRepository
import com.example.ihatemylife.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * ViewModel for NewChatActivity
 * Manages contact list, search, and sorting
 */
class NewChatViewModel(application: Application, val currentUserId: String) : AndroidViewModel(application) {
    private val contactRepository = ContactRepository(application)
    private val userRepository = UserRepository(application)
    
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _sortAscending = MutableStateFlow(true)
    val sortAscending: StateFlow<Boolean> = _sortAscending.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadContacts()
    }
    
    /**
     * Load contacts (user-created + system users)
     */
    private fun loadContacts() {
        viewModelScope.launch {
            // Combine user-created contacts and system users
            combine(
                contactRepository.getContactsForUser(currentUserId),
                userRepository.getAllUsersFlow()
            ) { userContacts, systemUsers ->
                // Convert system users to contacts
                val systemContacts = systemUsers.map { user ->
                    Contact(
                        id = user.id.toString(),
                        firstName = user.username,
                        lastName = "",
                        email = null,
                        phone = null,
                        username = user.username
                    )
                }
                // Combine and remove duplicates
                (userContacts + systemContacts).distinctBy { it.id }
            }.collect { allContacts ->
                _contacts.value = allContacts
            }
        }
    }
    
    /**
     * Get filtered and sorted contacts
     */
    fun getFilteredContacts(): List<Contact> {
        val query = _searchQuery.value.lowercase()
        val filtered = if (query.isBlank()) {
            _contacts.value
        } else {
            _contacts.value.filter {
                it.firstName.lowercase().contains(query) ||
                it.lastName.lowercase().contains(query) ||
                (it.email != null && it.email.lowercase().contains(query)) ||
                (it.phone != null && it.phone.contains(query)) ||
                (it.username != null && it.username.lowercase().contains(query))
            }
        }
        
        return if (_sortAscending.value) {
            filtered.sortedBy { "${it.firstName} ${it.lastName}".trim() }
        } else {
            filtered.sortedByDescending { "${it.firstName} ${it.lastName}".trim() }
        }
    }
    
    /**
     * Set search query
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    /**
     * Toggle sort order
     */
    fun toggleSort() {
        _sortAscending.value = !_sortAscending.value
    }
    
    /**
     * Add contact
     */
    fun addContact(contact: Contact, onResult: (Result<Contact>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = contactRepository.addContact(contact, currentUserId)
            onResult(result)
            _isLoading.value = false
        }
    }
}

