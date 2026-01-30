package com.example.ihatemylife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihatemylife.MessengerConstants
import com.example.ihatemylife.Message
import com.example.ihatemylife.repository.ChatRepository
import com.example.ihatemylife.repository.MessageRepository
import com.example.ihatemylife.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

    /** True if the other user sent a [TYPING] message within the active window. */
    private val _otherUserTyping = MutableStateFlow(false)
    val otherUserTyping: StateFlow<Boolean> = _otherUserTyping.asStateFlow()

    /** Last activity timestamp (epoch ms) for the other user; used for "last seen" presence. */
    private val _otherUserLastSeen = MutableStateFlow<Long?>(null)
    val otherUserLastSeen: StateFlow<Long?> = _otherUserLastSeen.asStateFlow()
    
    var currentUserId: Int? = null
        private set
    private var otherUserId: Int? = null

    private var typingJob: Job? = null
    
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
            
            // If we have both IDs, load conversation; then sync so we have latest from other device
            if (currentUserId != null && otherUserId != null) {
                loadConversation()
                syncMessages()
            } else if (currentUserId != null) {
                loadMessages()
                syncMessages()
            }
        }
    }
    
    /**
     * Filter out typing-indicator system messages and update typing/last-seen state.
     * Marks received messages as read so the other device sees read receipts.
     */
    private fun applyMessageList(raw: List<Message>) {
        val display = raw.filter { it.content != MessengerConstants.TYPING_INDICATOR_CONTENT }
        _messages.value = display
        val otherId = otherUserId ?: return
        val now = System.currentTimeMillis()
        val lastTypingFromOther = raw
            .filter { it.senderId == otherId && it.content == MessengerConstants.TYPING_INDICATOR_CONTENT }
            .maxByOrNull { it.timestamp }
        _otherUserTyping.value = lastTypingFromOther != null &&
            (now - lastTypingFromOther.timestamp) < MessengerConstants.TYPING_INDICATOR_ACTIVE_MS
        viewModelScope.launch {
            messageRepository.getLastActivityTimestampForUser(otherId)?.let { ts ->
                _otherUserLastSeen.value = ts
            }
        }
        // Mark received messages in this conversation as read (for read receipts on other device)
        viewModelScope.launch {
            display.filter { it.receiverId == currentUserId && !it.isRead }.forEach { msg ->
                messageRepository.markAsRead(msg.id, currentUsername)
            }
        }
    }
    
    /**
     * Load messages for this chat, including from integrated messengers (e.g. Telegram).
     * Filters out [TYPING] system messages and updates typing/last-seen state.
     */
    private fun loadMessages() {
        viewModelScope.launch {
            val userId = currentUserId ?: return@launch
            combine(
                messageRepository.getAllMessagesForUser(userId),
                messageRepository.getTelegramMessagesForUser(userId)
            ) { local, telegram ->
                val filtered = if (otherUserId != null) {
                    local.filter {
                        (it.senderId == currentUserId && it.receiverId == otherUserId) ||
                        (it.senderId == otherUserId && it.receiverId == currentUserId)
                    }
                } else {
                    local
                }
                (filtered + telegram).sortedBy { it.timestamp }
            }.collect { merged ->
                applyMessageList(merged)
            }
        }
    }
    
    /**
     * Load conversation between two users, including messages from integrated messengers (e.g. Telegram).
     * Filters out [TYPING] system messages and updates typing/last-seen state.
     */
    private fun loadConversation() {
        viewModelScope.launch {
            val userId1 = currentUserId ?: return@launch
            val userId2 = otherUserId ?: return@launch

            combine(
                messageRepository.getConversation(userId1, userId2),
                messageRepository.getTelegramMessagesForUser(userId1)
            ) { local, telegram ->
                (local + telegram).sortedBy { it.timestamp }
            }.collect { merged ->
                applyMessageList(merged)
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
            
            val replyToId = _replyToMessage.value?.id
            
            val result = messageRepository.sendMessage(
                senderUsername = currentUsername,
                receiverUsername = otherUsername,
                content = content,
                replyToMessageId = replyToId
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

    /**
     * Call when the user is typing. Sends a [TYPING] system message debounced so the other device can show "X is typing".
     * Call with false when user stops typing or sends a message.
     */
    fun setUserTyping(isTyping: Boolean) {
        if (otherUsername == null) return
        typingJob?.cancel()
        if (!isTyping) {
            _otherUserTyping.value = false
            return
        }
        typingJob = viewModelScope.launch {
            while (true) {
                messageRepository.sendMessage(
                    senderUsername = currentUsername,
                    receiverUsername = otherUsername!!,
                    content = MessengerConstants.TYPING_INDICATOR_CONTENT,
                    replyToMessageId = null
                )
                delay(MessengerConstants.TYPING_SEND_DEBOUNCE_MS)
            }
        }
    }

    /** Call when user stops typing (e.g. sent message or cleared input). */
    fun stopTyping() {
        typingJob?.cancel()
        typingJob = null
    }
}

