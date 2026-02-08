package com.example.ihatemylife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihatemylife.Chat
import com.example.ihatemylife.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for ChatsActivity
 * Manages chat list state and operations
 */
class ChatsViewModel(application: Application) : AndroidViewModel(application) {
    private val chatRepository = ChatRepository(application)
    
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadChats()
    }
    
    /**
     * Load active chats from repository
     */
    private fun loadChats() {
        viewModelScope.launch {
            chatRepository.getAllActiveChats().collect { chatList ->
                _chats.value = chatList
            }
        }
    }
    
    /**
     * Refresh chats
     */
    fun refreshChats() {
        loadChats()
    }
    
    /**
     * Set chat muted status
     */
    fun setChatMuted(chatId: String, isMuted: Boolean) {
        viewModelScope.launch {
            chatRepository.setMuted(chatId, isMuted)
        }
    }

    /**
     * Clear all chats (Room + in-memory). List will update via Flow.
     */
    fun clearAllChats() {
        viewModelScope.launch {
            chatRepository.clearAllChats()
            _chats.value = emptyList()
        }
    }
}

