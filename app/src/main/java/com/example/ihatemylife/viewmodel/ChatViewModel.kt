package com.example.ihatemylife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihatemylife.Message
import com.example.ihatemylife.repository.ChatRepository
import com.example.ihatemylife.repository.MessageRepository
import com.example.ihatemylife.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for ChatActivity
 * Manages message list, sending messages, and conversation state
 */
class ChatViewModel(
    application: Application,
    val chatId: String,
    val currentUsername: String,
    val otherUsername: String?
) : AndroidViewModel(application) {
    private val messageRepository = MessageRepository(application)
    private val chatRepository = ChatRepository(application)
    private val userRepository = UserRepository(application)
    
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _replyToMessage = MutableStateFlow<Message?>(null)
    val replyToMessage: StateFlow<Message?> = _replyToMessage.asStateFlow()
    
    var currentUserId: Int? = null
        private set
    private var otherUserId: Int? = null
    
    init {
        loadUserIds()
    }
    
    /**
     * Load user IDs for current and other user
     */
    private fun loadUserIds() {
        viewModelScope.launch {
            currentUserId = userRepository.getUserByUsername(currentUsername)?.id
            if (otherUsername != null) {
                otherUserId = userRepository.getUserByUsername(otherUsername)?.id
            }
            
            // If we have both IDs, load conversation
            if (currentUserId != null && otherUserId != null) {
                loadConversation()
            } else if (currentUserId != null) {
                loadMessages()
            }
        }
    }
    
    /**
     * Load messages for this chat
     */
    private fun loadMessages() {
        viewModelScope.launch {
            currentUserId?.let { userId ->
                messageRepository.getAllMessagesForUser(userId).collect { messageList ->
                    // Filter messages for this conversation if we have other user
                    if (otherUserId != null) {
                        _messages.value = messageList.filter {
                            (it.senderId == currentUserId && it.receiverId == otherUserId) ||
                            (it.senderId == otherUserId && it.receiverId == currentUserId)
                        }
                    } else {
                        _messages.value = messageList
                    }
                }
            }
        }
    }
    
    /**
     * Load conversation between two users
     */
    private fun loadConversation() {
        viewModelScope.launch {
            val userId1 = currentUserId ?: return@launch
            val userId2 = otherUserId ?: return@launch
            
            messageRepository.getConversation(userId1, userId2).collect { messageList ->
                _messages.value = messageList
            }
        }
    }
    
    /**
     * Send a message
     */
    fun sendMessage(content: String) {
        if (content.isBlank() || otherUsername == null) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = messageRepository.sendMessage(
                senderUsername = currentUsername,
                receiverUsername = otherUsername,
                content = content
            )
            
            result.onSuccess { message ->
                // Update chat's last message
                chatRepository.updateLastMessage(chatId, content, message.timestamp)
                // Clear reply if any
                _replyToMessage.value = null
            }.onFailure { exception ->
                _error.value = exception.message ?: "Failed to send message"
            }
            
            _isLoading.value = false
        }
    }
    
    /**
     * Set message to reply to
     */
    fun setReplyToMessage(message: Message?) {
        _replyToMessage.value = message
    }
    
    /**
     * Sync messages from backend
     */
    fun syncMessages() {
        viewModelScope.launch {
            _isLoading.value = true
            messageRepository.syncMessages(currentUsername).onFailure { exception ->
                _error.value = exception.message
            }
            _isLoading.value = false
        }
    }
    
    /**
     * Mark message as read
     */
    fun markAsRead(messageId: Int) {
        viewModelScope.launch {
            messageRepository.markAsRead(messageId, currentUsername)
        }
    }
}

