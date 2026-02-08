package com.example.ihatemylife.database;

/**
 * Room database for local data persistence
 * Offline-first approach: cache API data locally
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&\u00a8\u0006\u000e"}, d2 = {"Lcom/example/ihatemylife/database/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "chatDao", "Lcom/example/ihatemylife/database/dao/ChatDao;", "contactDao", "Lcom/example/ihatemylife/database/dao/ContactDao;", "localChatMessageDao", "Lcom/example/ihatemylife/database/dao/LocalChatMessageDao;", "messageDao", "Lcom/example/ihatemylife/database/dao/MessageDao;", "userDao", "Lcom/example/ihatemylife/database/dao/UserDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.example.ihatemylife.database.entities.UserEntity.class, com.example.ihatemylife.database.entities.MessageEntity.class, com.example.ihatemylife.database.entities.ChatEntity.class, com.example.ihatemylife.database.entities.ContactEntity.class, com.example.ihatemylife.database.entities.LocalChatMessageEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.ihatemylife.database.AppDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ihatemylife.database.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.ihatemylife.database.dao.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.ihatemylife.database.dao.MessageDao messageDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.ihatemylife.database.dao.ChatDao chatDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.ihatemylife.database.dao.ContactDao contactDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.ihatemylife.database.dao.LocalChatMessageDao localChatMessageDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/ihatemylife/database/AppDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/example/ihatemylife/database/AppDatabase;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.ihatemylife.database.AppDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}