package com.example.ihatemylife.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0014\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nH\'J\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\n2\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u0016\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u001c\u0010\u0013\u001a\u00020\u00032\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u001e\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u0019H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u001e\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u0019H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u0012J&\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020!H\u00a7@\u00a2\u0006\u0002\u0010\"\u00a8\u0006#"}, d2 = {"Lcom/example/ihatemylife/database/dao/ChatDao;", "", "deleteAllChats", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteChat", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllActiveChats", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/ihatemylife/database/entities/ChatEntity;", "getAllActiveChatsList", "getChatById", "getChatByIdFlow", "insertChat", "chat", "(Lcom/example/ihatemylife/database/entities/ChatEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertChats", "chats", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setActive", "chatId", "isActive", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setMuted", "isMuted", "updateChat", "updateLastMessage", "message", "timestamp", "", "(Ljava/lang/String;Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface ChatDao {
    
    @androidx.room.Query(value = "SELECT * FROM chats WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getChatById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ihatemylife.database.entities.ChatEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM chats WHERE id = :id")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.example.ihatemylife.database.entities.ChatEntity> getChatByIdFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String id);
    
    @androidx.room.Query(value = "SELECT * FROM chats WHERE isActive = 1 ORDER BY lastMessageTimestamp DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.database.entities.ChatEntity>> getAllActiveChats();
    
    @androidx.room.Query(value = "SELECT * FROM chats WHERE isActive = 1 ORDER BY lastMessageTimestamp DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllActiveChatsList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ihatemylife.database.entities.ChatEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertChat(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.database.entities.ChatEntity chat, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertChats(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.ihatemylife.database.entities.ChatEntity> chats, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateChat(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.database.entities.ChatEntity chat, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE chats SET lastMessage = :message, lastMessageTimestamp = :timestamp WHERE id = :chatId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId, @org.jetbrains.annotations.NotNull()
    java.lang.String message, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE chats SET isMuted = :isMuted WHERE id = :chatId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setMuted(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId, boolean isMuted, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE chats SET isActive = :isActive WHERE id = :chatId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setActive(@org.jetbrains.annotations.NotNull()
    java.lang.String chatId, boolean isActive, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM chats WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteChat(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM chats")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllChats(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}