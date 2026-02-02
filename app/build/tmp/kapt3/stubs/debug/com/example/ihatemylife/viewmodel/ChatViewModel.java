package com.example.ihatemylife.viewmodel;

/**
 * ViewModel for ChatActivity
 * Manages message list, sending messages, and conversation state
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000f\b\u0007\u0018\u00002\u00020\u0001B\'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\bJ\u0016\u00104\u001a\u0002052\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0002J\b\u00107\u001a\u000205H\u0002J\b\u00108\u001a\u000205H\u0002J\b\u00109\u001a\u000205H\u0002J\u000e\u0010:\u001a\u0002052\u0006\u0010;\u001a\u00020\u0019J\u000e\u0010<\u001a\u0002052\u0006\u0010=\u001a\u00020\u0005J\u0010\u0010>\u001a\u0002052\b\u0010?\u001a\u0004\u0018\u00010\u000fJ\u000e\u0010@\u001a\u0002052\u0006\u0010A\u001a\u00020\fJ\u0006\u0010B\u001a\u000205J\u0006\u0010C\u001a\u000205R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\u001a\u001a\u0004\u0018\u00010\u00192\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019@BX\u0086\u000e\u00a2\u0006\n\n\u0002\u0010\u001d\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0015R\u0019\u0010\u001f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050 \u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020\f0 \u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\"R\u000e\u0010$\u001a\u00020%X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0 \u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\"R\u0012\u0010(\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001dR\u0019\u0010)\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110 \u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\"R\u0017\u0010+\u001a\b\u0012\u0004\u0012\u00020\f0 \u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\"R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u0015R\u0019\u0010.\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0 \u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\"R\u0010\u00100\u001a\u0004\u0018\u000101X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u000203X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006D"}, d2 = {"Lcom/example/ihatemylife/viewmodel/ChatViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "chatId", "", "currentUsername", "otherUsername", "(Landroid/app/Application;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "_error", "Lkotlinx/coroutines/flow/MutableStateFlow;", "_isLoading", "", "_messages", "", "Lcom/example/ihatemylife/Message;", "_otherUserLastSeen", "", "_otherUserTyping", "_replyToMessage", "getChatId", "()Ljava/lang/String;", "chatRepository", "Lcom/example/ihatemylife/repository/ChatRepository;", "<set-?>", "", "currentUserId", "getCurrentUserId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getCurrentUsername", "error", "Lkotlinx/coroutines/flow/StateFlow;", "getError", "()Lkotlinx/coroutines/flow/StateFlow;", "isLoading", "messageRepository", "Lcom/example/ihatemylife/repository/MessageRepository;", "messages", "getMessages", "otherUserId", "otherUserLastSeen", "getOtherUserLastSeen", "otherUserTyping", "getOtherUserTyping", "getOtherUsername", "replyToMessage", "getReplyToMessage", "typingJob", "Lkotlinx/coroutines/Job;", "userRepository", "Lcom/example/ihatemylife/repository/UserRepository;", "applyMessageList", "", "raw", "loadConversation", "loadMessages", "loadUserIds", "markAsRead", "messageId", "sendMessage", "content", "setReplyToMessage", "message", "setUserTyping", "isTyping", "stopTyping", "syncMessages", "app_debug"})
public final class ChatViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String chatId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentUsername = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String otherUsername = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.repository.MessageRepository messageRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.repository.ChatRepository chatRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.repository.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.example.ihatemylife.Message>> _messages = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.ihatemylife.Message>> messages = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _error = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> error = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.ihatemylife.Message> _replyToMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.ihatemylife.Message> replyToMessage = null;
    
    /**
     * True if the other user sent a [TYPING] message within the active window.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _otherUserTyping = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> otherUserTyping = null;
    
    /**
     * Last activity timestamp (epoch ms) for the other user; used for "last seen" presence.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _otherUserLastSeen = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> otherUserLastSeen = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer currentUserId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer otherUserId;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job typingJob;
    
    public ChatViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application, @org.jetbrains.annotations.NotNull()
    java.lang.String chatId, @org.jetbrains.annotations.NotNull()
    java.lang.String currentUsername, @org.jetbrains.annotations.Nullable()
    java.lang.String otherUsername) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getChatId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentUsername() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOtherUsername() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.ihatemylife.Message>> getMessages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.ihatemylife.Message> getReplyToMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getOtherUserTyping() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getOtherUserLastSeen() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getCurrentUserId() {
        return null;
    }
    
    /**
     * Load user IDs for current and other user
     */
    private final void loadUserIds() {
    }
    
    /**
     * Filter out typing-indicator system messages and update typing/last-seen state.
     * Marks received messages as read so the other device sees read receipts.
     */
    private final void applyMessageList(java.util.List<com.example.ihatemylife.Message> raw) {
    }
    
    /**
     * Load messages for this chat, including from integrated messengers (e.g. Telegram).
     * Filters out [TYPING] system messages and updates typing/last-seen state.
     */
    private final void loadMessages() {
    }
    
    /**
     * Load conversation between two users, including messages from integrated messengers (e.g. Telegram).
     * Filters out [TYPING] system messages and updates typing/last-seen state.
     */
    private final void loadConversation() {
    }
    
    /**
     * Send a message
     */
    public final void sendMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String content) {
    }
    
    /**
     * Set message to reply to
     */
    public final void setReplyToMessage(@org.jetbrains.annotations.Nullable()
    com.example.ihatemylife.Message message) {
    }
    
    /**
     * Sync messages from backend
     */
    public final void syncMessages() {
    }
    
    /**
     * Mark message as read
     */
    public final void markAsRead(int messageId) {
    }
    
    /**
     * Call when the user is typing. Sends a [TYPING] system message debounced so the other device can show "X is typing".
     * Call with false when user stops typing or sends a message.
     */
    public final void setUserTyping(boolean isTyping) {
    }
    
    /**
     * Call when user stops typing (e.g. sent message or cleared input).
     */
    public final void stopTyping() {
    }
}