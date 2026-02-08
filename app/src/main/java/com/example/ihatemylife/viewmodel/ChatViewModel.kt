package com.example.ihatemylife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihatemylife.MessengerConstants
import com.example.ihatemylife.Message
import com.example.ihatemylife.SessionMessageStore
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
 * ViewModel for ChatActivity.
 * Manages message list for this chat only; messages are stored and displayed
 * only within this chat session and are not shared with other chats.
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
    
    /** Isolated message list for this chat only. Never contains messages from other chats. */
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

    /** True when this chat is with a created contact (no backend user). All messages in this chat are from the current user. */
    val isCreatedContactChat: Boolean get() = otherUserId == null

    private var typingJob: Job? = null
    
    init {
        loadUserIds()
    }

    override fun onCleared() {
        super.onCleared()
        SessionMessageStore.clearChat(chatId)
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
            } else if (otherUserId == null) {
                // Created-contact chat and current user not in Room: still load local messages
                loadMessages()
            }
        }
    }
    
    /**
     * Returns true if the message belongs to this chat's conversation (currentUserId <-> otherUserId).
     */
    private fun belongsToThisConversation(msg: Message): Boolean {
        val cur = currentUserId ?: return false
        val other = otherUserId ?: return false
        return (msg.senderId == cur && msg.receiverId == other) ||
            (msg.senderId == other && msg.receiverId == cur)
    }

    /**
     * Filter to this chat only, merge session-local messages, drop typing indicators, update UI.
     * Messages from other chats are never included.
     * For local-only chats (otherUserId == null), pass raw as the list for this chat; belongsToThisConversation is not used there.
     */
    private fun applyMessageList(raw: List<Message>) {
        val onlyThisChat = if (otherUserId != null) raw.filter { belongsToThisConversation(it) } else raw
        val sessionLocal = SessionMessageStore.getMessagesForChat(chatId)
        val merged = (onlyThisChat + sessionLocal)
            .distinctBy { it.id }
            .filter { it.content != MessengerConstants.TYPING_INDICATOR_CONTENT }
            .sortedBy { it.timestamp }
        _messages.value = merged
        val otherId = otherUserId ?: return
        val now = System.currentTimeMillis()
        val lastTypingFromOther = onlyThisChat
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
            merged.filter { it.receiverId == currentUserId && !it.isRead && it.id > 0 }.forEach { msg ->
                messageRepository.markAsRead(msg.id, currentUsername)
            }
        }
    }
    
    /**
     * Load messages for this chat only. When we have no other user (created contact), load from
     * local chat message store and session. Otherwise load from Room/conversation and merge.
     */
    private fun loadMessages() {
        viewModelScope.launch {
            if (otherUserId == null) {
                // Created-contact chat: display persisted local messages + session (no DB user required)
                messageRepository.getLocalMessagesForChat(chatId).collect { localList ->
                    val sessionLocal = SessionMessageStore.getMessagesForChat(chatId)
                    val merged = (localList + sessionLocal)
                        .distinctBy { it.id }
                        .filter { it.content != MessengerConstants.TYPING_INDICATOR_CONTENT }
                        .sortedBy { it.timestamp }
                    _messages.value = merged
                }
                return@launch
            }
            val userId = currentUserId ?: return@launch
            combine(
                messageRepository.getAllMessagesForUser(userId),
                messageRepository.getTelegramMessagesForUser(userId)
            ) { local, telegram ->
                val filtered = local.filter {
                    (it.senderId == currentUserId && it.receiverId == otherUserId) ||
                    (it.senderId == otherUserId && it.receiverId == currentUserId)
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
     * Fallback sender id when current user is not in Room/DB (e.g. only in DatabaseHelper).
     * Used for created-contact chats so messages can still be sent and stored.
     */
    private val localSenderIdFallback = 0

    /**
     * Send a message. For backend users: optimistic UI then API. For created contacts (local-only): persist locally and show.
     * Created-contact chats work even when current user is not in DB (uses fallback sender id).
     */
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        val replyToId = _replyToMessage.value?.id

        if (otherUserId == null) {
            // Local-only chat (created contact): persist to local store and update UI.
            // Use fallback sender id when current user is not in Room so message still sends and shows.
            val curId = currentUserId ?: localSenderIdFallback
            val optimistic = Message(
                id = -(System.currentTimeMillis() and 0x7FFFFFFF).toInt(),
                senderId = curId,
                receiverId = null,
                content = content,
                timestamp = System.currentTimeMillis(),
                isDelivered = true,
                isRead = false,
                replyToMessageId = replyToId
            )
            SessionMessageStore.addMessage(chatId, optimistic)
            applyMessageList(_messages.value + optimistic)
            viewModelScope.launch {
                _isLoading.value = true
                _error.value = null
                val message = messageRepository.insertLocalMessage(
                    chatId = chatId,
                    senderId = curId,
                    content = content,
                    timestamp = optimistic.timestamp,
                    replyToMessageId = replyToId
                )
                SessionMessageStore.removeMessage(chatId, optimistic.id)
                chatRepository.updateLastMessage(chatId, content, message.timestamp)
                _replyToMessage.value = null
                _isLoading.value = false
            }
            return
        }

        val curId = currentUserId ?: return
        val otherId = otherUserId!!
        val optimistic = Message(
            id = -(System.currentTimeMillis() and 0x7FFFFFFF).toInt(),
            senderId = curId,
            receiverId = otherId,
            content = content,
            timestamp = System.currentTimeMillis(),
            isDelivered = false,
            isRead = false,
            replyToMessageId = replyToId
        )
        SessionMessageStore.addMessage(chatId, optimistic)
        applyMessageList(_messages.value + optimistic)

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = messageRepository.sendMessage(
                senderUsername = currentUsername,
                receiverUsername = otherUsername!!,
                content = content,
                replyToMessageId = replyToId
            )

            result.onSuccess { message ->
                SessionMessageStore.removeMessage(chatId, optimistic.id)
                chatRepository.updateLastMessage(chatId, content, message.timestamp)
                _replyToMessage.value = null
            }.onFailure { exception ->
                SessionMessageStore.removeMessage(chatId, optimistic.id)
                _error.value = exception.message ?: "Failed to send message"
                _messages.value = _messages.value.filter { it.id != optimistic.id }
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

