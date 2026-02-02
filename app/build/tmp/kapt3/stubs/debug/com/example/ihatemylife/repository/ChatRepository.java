package com.example.ihatemylife.repository;

/**
 * Repository for chat operations
 * Manages chat list and updates based on messages
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u000fH\u0002J\u0012\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00160\u0015J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0018\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0019J\u001e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\u001eH\u0086@\u00a2\u0006\u0002\u0010\u001fJ\u001e\u0010 \u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010!\u001a\u00020\u001eH\u0086@\u00a2\u0006\u0002\u0010\u001fJ&\u0010\"\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010#\u001a\u00020\n2\u0006\u0010$\u001a\u00020%H\u0086@\u00a2\u0006\u0002\u0010&J\u0016\u0010\'\u001a\u00020\u001b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010(R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lcom/example/ihatemylife/repository/ChatRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "chatDao", "Lcom/example/ihatemylife/database/dao/ChatDao;", "messageDao", "Lcom/example/ihatemylife/database/dao/MessageDao;", "createChatId", "", "userId1", "", "userId2", "domainToEntity", "Lcom/example/ihatemylife/database/entities/ChatEntity;", "chat", "Lcom/example/ihatemylife/Chat;", "entityToDomain", "entity", "getAllActiveChats", "Lkotlinx/coroutines/flow/Flow;", "", "getChatById", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setActive", "", "chatId", "isActive", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setMuted", "isMuted", "updateLastMessage", "message", "timestamp", "", "(Ljava/lang/String;Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertChat", "(Lcom/example/ihatemylife/Chat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ChatRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.database.dao.ChatDao chatDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.database.dao.MessageDao messageDao = null;
    
    public ChatRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Convert entity to domain Chat
     */
    private final com.example.ihatemylife.Chat entityToDomain(com.example.ihatemylife.database.entities.ChatEntity entity) {
        return null;
    }
    
    /**
     * Convert domain Chat to entity
     */
    private final com.example.ihatemylife.database.entities.ChatEntity domainToEntity(com.example.ihatemylife.Chat chat) {
        return null;
    }
    
    /**
     * Get all active chats
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.Chat>> getAllActiveChats() {
        return null;
    }
    
    /**
     * Get chat by ID
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getChatById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ihatemylife.Chat> $completion) {
        return null;
    }
    
    /**
     * Create or update chat
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object upsertChat(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.Chat chat, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Update chat's last message
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateLastMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId, @org.jetbrains.annotations.NotNull()
    java.lang.String message, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Set chat muted status
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setMuted(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId, boolean isMuted, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Set chat active status
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setActive(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId, boolean isActive, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Create chat ID for conversation between two users
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String createChatId(int userId1, int userId2) {
        return null;
    }
}