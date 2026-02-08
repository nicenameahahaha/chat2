package com.example.ihatemylife.repository;

/**
 * Repository for user operations
 * Combines local (Room) and remote (API) data sources
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u0012\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\rJ\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\r2\u0006\u0010\u0011\u001a\u00020\u0012J,\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u0012H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0019\u0010\u001aJ\u001c\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\n0\u0016H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001c\u0010\u000bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001d"}, d2 = {"Lcom/example/ihatemylife/repository/UserRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "apiService", "Lcom/example/ihatemylife/api/ApiService;", "userDao", "Lcom/example/ihatemylife/database/dao/UserDao;", "clearAllUsers", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllUsersFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/ihatemylife/database/entities/UserEntity;", "getUserByUsername", "username", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserByUsernameFlow", "registerUser", "Lkotlin/Result;", "Lcom/example/ihatemylife/api/models/ApiUserOut;", "password", "registerUser-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncUsers", "syncUsers-IoAF18A", "app_debug"})
public final class UserRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.api.ApiService apiService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.database.dao.UserDao userDao = null;
    
    public UserRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Get user by username from local database
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUserByUsername(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ihatemylife.database.entities.UserEntity> $completion) {
        return null;
    }
    
    /**
     * Get user by username as Flow
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.example.ihatemylife.database.entities.UserEntity> getUserByUsernameFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String username) {
        return null;
    }
    
    /**
     * Get all users from local database
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.ihatemylife.database.entities.UserEntity>> getAllUsersFlow() {
        return null;
    }
    
    /**
     * Delete all users from the local database.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearAllUsers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}