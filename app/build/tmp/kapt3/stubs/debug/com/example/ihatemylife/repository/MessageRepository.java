package com.example.ihatemylife.repository;

/**
 * Repository for message operations
 * Handles sending/receiving messages and syncing with backend
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u000e\u0010\u0015\u001a\u00020\u0016H\u0082@\u00a2\u0006\u0002\u0010\u0017J\u0010\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u0019\u001a\u00020\u0014H\u0002J\u001a\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u001c0\u001b2\u0006\u0010\u001d\u001a\u00020\u001eJ\"\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u001c0\u001b2\u0006\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u001eJ\u0018\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010\u001d\u001a\u00020\u001eH\u0086@\u00a2\u0006\u0002\u0010$J\u001a\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u001c0\u001b2\u0006\u0010&\u001a\u00020\'J\u001a\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u001c0\u001b2\u0006\u0010\u001d\u001a\u00020\u001eJ<\u0010)\u001a\u00020\u00102\u0006\u0010&\u001a\u00020\'2\u0006\u0010*\u001a\u00020\u001e2\u0006\u0010+\u001a\u00020\'2\b\b\u0002\u0010,\u001a\u00020#2\n\b\u0002\u0010-\u001a\u0004\u0018\u00010\u001eH\u0086@\u00a2\u0006\u0002\u0010.J\u0010\u0010/\u001a\u00020\u00102\u0006\u0010\u0019\u001a\u000200H\u0002J$\u00101\u001a\b\u0012\u0004\u0012\u00020\u0016022\u0006\u00103\u001a\u00020\u001eH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b4\u0010$J,\u00105\u001a\b\u0012\u0004\u0012\u00020\u0016022\u0006\u00103\u001a\u00020\u001e2\u0006\u00106\u001a\u00020\'H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b7\u00108J\u0010\u00109\u001a\u00020#2\u0006\u0010,\u001a\u00020\'H\u0002J@\u0010:\u001a\b\u0012\u0004\u0012\u00020\u0010022\u0006\u0010;\u001a\u00020\'2\u0006\u0010<\u001a\u00020\'2\u0006\u0010+\u001a\u00020\'2\n\b\u0002\u0010-\u001a\u0004\u0018\u00010\u001eH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b=\u0010>J$\u0010?\u001a\b\u0012\u0004\u0012\u00020\u0016022\u0006\u00106\u001a\u00020\'H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b@\u0010AR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006B"}, d2 = {"Lcom/example/ihatemylife/repository/MessageRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "apiService", "Lcom/example/ihatemylife/api/ApiService;", "dateFormat", "Ljava/text/SimpleDateFormat;", "localChatMessageDao", "Lcom/example/ihatemylife/database/dao/LocalChatMessageDao;", "messageDao", "Lcom/example/ihatemylife/database/dao/MessageDao;", "userDao", "Lcom/example/ihatemylife/database/dao/UserDao;", "apiMessageToDomain", "Lcom/example/ihatemylife/Message;", "apiMessage", "Lcom/example/ihatemylife/api/models/ApiMessageOut;", "apiMessageToEntity", "Lcom/example/ihatemylife/database/entities/MessageEntity;", "ensureUsersFromMessages", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "entityToDomain", "entity", "getAllMessagesForUser", "Lkotlinx/coroutines/flow/Flow;", "", "userId", "", "getConversation", "userId1", "userId2", "getLastActivityTimestampForUser", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLocalMessagesForChat", "chatId", "", "getTelegramMessagesForUser", "insertLocalMessage", "senderId", "content", "timestamp", "replyToMessageId", "(Ljava/lang/String;ILjava/lang/String;JLjava/lang/Integer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "localEntityToMessage", "Lcom/example/ihatemylife/database/entities/LocalChatMessageEntity;", "markAsDelivered", "Lkotlin/Result;", "messageId", "markAsDelivered-gIAlu-s", "markAsRead", "username", "markAsRead-0E7RQCE", "(ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseTimestamp", "sendMessage", "senderUsername", "receiverUsername", "sendMessage-yxL6bBk", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncMessages", "syncMessages-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class MessageRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.api.ApiService apiService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.database.dao.MessageDao messageDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.database.dao.UserDao userDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.database.dao.LocalChatMessageDao localChatMessageDao = null;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat dateFormat = null;
    
    public MessageRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Convert API message to domain Message
     */
    private final com.example.ihatemylife.Message apiMessageToDomain(com.example.ihatemylife.api.models.ApiMessageOut apiMessage) {
        return null;
    }
    
    /**
     * Convert API message to entity
     */
    private final com.example.ihatemylife.database.entities.MessageEntity apiMessageToEntity(com.example.ihatemylife.api.models.ApiMessageOut apiMessage) {
        return null;
    }
    
    /**
     * Convert entity to domain Message
     */
    private final com.example.ihatemylife.Message entityToDomain(com.example.ihatemylife.database.entities.MessageEntity entity) {
        return null;
    }
    
    /**
     * Parse ISO datetime string to epoch milliseconds
     */
    private final long parseTimestamp(java.lang.String timestamp) {
        return 0L;
    }
    
    /**
     * Get conversation between two users
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.Message>> getConversation(int userId1, int userId2) {
        return null;
    }
    
    /**
     * Get all messages for a user
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.Message>> getAllMessagesForUser(int userId) {
        return null;
    }
    
    /**
     * Get messages from Telegram integration for this user (sender or receiver).
     * Data comes from local DB after sync; backend includes Telegram messages in "sent" for the linked user.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.Message>> getTelegramMessagesForUser(int userId) {
        return null;
    }
    
    /**
     * Get persisted local messages for a chat (e.g. created-contact chats with no backend user).
     * Used when otherUserId is null so messages are stored only by chatId.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.Message>> getLocalMessagesForChat(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId) {
        return null;
    }
    
    /**
     * Persist a message for a local-only chat. Call when otherUserId is null (created contact).
     * Returns the domain Message with a negative id (local messages use negative ids to avoid clashing with backend).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertLocalMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId, int senderId, @org.jetbrains.annotations.NotNull()
    java.lang.String content, long timestamp, @org.jetbrains.annotations.Nullable()
    java.lang.Integer replyToMessageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ihatemylife.Message> $completion) {
        return null;
    }
    
    private final com.example.ihatemylife.Message localEntityToMessage(com.example.ihatemylife.database.entities.LocalChatMessageEntity entity) {
        return null;
    }
    
    /**
     * Ensures all user IDs that appear in messages exist in the users table.
     * Inserts placeholder UserEntity(id, "User$id") for unknown IDs so the pseudo-global list grows from sync.
     */
    private final java.lang.Object ensureUsersFromMessages(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Last activity timestamp for a user (presence / last seen).
     * Returns the latest message timestamp where the user is sender or receiver.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLastActivityTimestampForUser(int userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
}