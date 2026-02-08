package com.example.ihatemylife.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u001e\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u000bJ,\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\u000e\u001a\u0004\u0018\u00010\u00072\b\u0010\u000f\u001a\u0004\u0018\u00010\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u0010J \u0010\u0011\u001a\u0004\u0018\u00010\r2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u00132\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u001c\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00130\u00152\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u0016\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u0018J\u001c\u0010\u0019\u001a\u00020\u00032\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\r0\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u0018\u00a8\u0006\u001d"}, d2 = {"Lcom/example/ihatemylife/database/dao/ContactDao;", "", "deleteAllContacts", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllContactsForUser", "userId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteContact", "id", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findContactByEmailOrPhone", "Lcom/example/ihatemylife/database/entities/ContactEntity;", "email", "phone", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getContactById", "getContactsForUser", "", "getContactsForUserFlow", "Lkotlinx/coroutines/flow/Flow;", "insertContact", "contact", "(Lcom/example/ihatemylife/database/entities/ContactEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertContacts", "contacts", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateContact", "app_debug"})
@androidx.room.Dao()
public abstract interface ContactDao {
    
    @androidx.room.Query(value = "SELECT * FROM contacts WHERE id = :id AND userId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getContactById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ihatemylife.database.entities.ContactEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM contacts WHERE userId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getContactsForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ihatemylife.database.entities.ContactEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM contacts WHERE userId = :userId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.database.entities.ContactEntity>> getContactsForUserFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String userId);
    
    @androidx.room.Query(value = "SELECT * FROM contacts WHERE userId = :userId AND (email = :email OR phone = :phone)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findContactByEmailOrPhone(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.Nullable()
    java.lang.String email, @org.jetbrains.annotations.Nullable()
    java.lang.String phone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ihatemylife.database.entities.ContactEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertContact(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.database.entities.ContactEntity contact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertContacts(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.ihatemylife.database.entities.ContactEntity> contacts, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateContact(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.database.entities.ContactEntity contact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM contacts WHERE id = :id AND userId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteContact(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM contacts WHERE userId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllContactsForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM contacts")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllContacts(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}