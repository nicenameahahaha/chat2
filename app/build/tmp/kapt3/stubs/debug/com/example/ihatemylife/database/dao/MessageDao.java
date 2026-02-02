package com.example.ihatemylife.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0013\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\u0005H\'J$\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0005H\'J\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\tH\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0013\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J$\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0012H\'J\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\u0005H\'J\u001c\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0018\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\u0005H\'J\u0016\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u001cJ\u001c\u0010\u001d\u001a\u00020\u00032\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u0016\u0010 \u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010!\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010#J\u0016\u0010$\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u001c\u00a8\u0006%"}, d2 = {"Lcom/example/ihatemylife/database/dao/MessageDao;", "", "deleteMessage", "", "id", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllMessagesForUser", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/ihatemylife/database/entities/MessageEntity;", "userId", "getConversation", "userId1", "userId2", "getDistinctUserIdsFromMessages", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLastActivityTimestampForUser", "", "getMessageById", "getMessagesAfter", "fromTimestamp", "getReceivedMessages", "getRepliesToMessage", "messageId", "getSentMessages", "insertMessage", "message", "(Lcom/example/ihatemylife/database/entities/MessageEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMessages", "messages", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markAsDelivered", "markAsRead", "readAt", "(IJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMessage", "app_debug"})
@androidx.room.Dao()
public abstract interface MessageDao {
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMessageById(int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ihatemylife.database.entities.MessageEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE senderId = :userId OR receiverId = :userId ORDER BY timestamp ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.database.entities.MessageEntity>> getAllMessagesForUser(int userId);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE (senderId = :userId1 AND receiverId = :userId2) OR (senderId = :userId2 AND receiverId = :userId1) ORDER BY timestamp ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.database.entities.MessageEntity>> getConversation(int userId1, int userId2);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE senderId = :userId ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.database.entities.MessageEntity>> getSentMessages(int userId);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE receiverId = :userId ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.database.entities.MessageEntity>> getReceivedMessages(int userId);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE (senderId = :userId OR receiverId = :userId) AND timestamp >= :fromTimestamp ORDER BY timestamp ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.database.entities.MessageEntity>> getMessagesAfter(int userId, long fromTimestamp);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMessage(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.database.entities.MessageEntity message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMessages(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.ihatemylife.database.entities.MessageEntity> messages, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMessage(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.database.entities.MessageEntity message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE messages SET isRead = 1, readAt = :readAt WHERE id = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAsRead(int messageId, long readAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE messages SET isDelivered = 1 WHERE id = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAsDelivered(int messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM messages WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMessage(int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE replyToMessageId = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRepliesToMessage(int messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ihatemylife.database.entities.MessageEntity>> $completion);
    
    /**
     * Last activity timestamp for presence (last seen).
     * Returns the latest message timestamp where the user is sender or receiver.
     */
    @androidx.room.Query(value = "SELECT MAX(timestamp) FROM messages WHERE senderId = :userId OR receiverId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLastActivityTimestampForUser(int userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Distinct user IDs that appear in messages (senders and receivers).
     * Used to discover users from synced messages and insert placeholders into users table.
     */
    @androidx.room.Query(value = "SELECT DISTINCT senderId FROM messages UNION SELECT DISTINCT receiverId FROM messages WHERE receiverId IS NOT NULL")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDistinctUserIdsFromMessages(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.Integer>> $completion);
}