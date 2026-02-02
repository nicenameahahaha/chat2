package com.example.ihatemylife.repository;

/**
 * Repository for contact operations
 * Manages user-created contacts (per-user scoped)
 * Combines local (Room) and remote (API) data sources
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J8\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0011\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0015J\u001e\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0018J\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0010\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u001aH\u0002J\u001a\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u001f0\u001e2\u0006\u0010\r\u001a\u00020\u000eJ,\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00140\n2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u0010H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b!\u0010\"R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006#"}, d2 = {"Lcom/example/ihatemylife/repository/ContactRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "apiService", "Lcom/example/ihatemylife/api/ApiService;", "contactDao", "Lcom/example/ihatemylife/database/dao/ContactDao;", "addContact", "Lkotlin/Result;", "Lcom/example/ihatemylife/Contact;", "contact", "userId", "", "backendUserId", "", "addContact-BWLJW6A", "(Lcom/example/ihatemylife/Contact;Ljava/lang/String;Ljava/lang/Integer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearContactsForUser", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteContact", "contactId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "domainToEntity", "Lcom/example/ihatemylife/database/entities/ContactEntity;", "entityToDomain", "entity", "getContactsForUser", "Lkotlinx/coroutines/flow/Flow;", "", "syncContactToBackend", "syncContactToBackend-0E7RQCE", "(Lcom/example/ihatemylife/Contact;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ContactRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.api.ApiService apiService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.database.dao.ContactDao contactDao = null;
    
    public ContactRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Convert entity to domain Contact
     */
    private final com.example.ihatemylife.Contact entityToDomain(com.example.ihatemylife.database.entities.ContactEntity entity) {
        return null;
    }
    
    /**
     * Convert domain Contact to entity
     */
    private final com.example.ihatemylife.database.entities.ContactEntity domainToEntity(com.example.ihatemylife.Contact contact, java.lang.String userId) {
        return null;
    }
    
    /**
     * Get all contacts for a user
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.Contact>> getContactsForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    /**
     * Delete contact
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteContact(@org.jetbrains.annotations.NotNull()
    java.lang.String contactId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Clear all contacts for a user
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearContactsForUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}