package com.example.ihatemylife.api;

/**
 * API configuration - base URL for the backend.
 * Default: http://127.0.0.1:8000 (e.g. FastAPI backend with users + messages).
 * Backend: users (POST /users/), messages (send, sent, received, all, conversation, read, delivered).
 * No contacts API; contacts are stored locally only.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\r\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/example/ihatemylife/api/ApiConfig;", "", "()V", "DEFAULT_BASE_URL", "", "DEFAULT_TELEGRAM_BOT_USERNAME", "KEY_BASE_URL", "KEY_TELEGRAM_BOT_USERNAME", "PREFS_NAME", "getBaseUrl", "context", "Landroid/content/Context;", "getTelegramBotUsername", "getTelegramConnectUrl", "setBaseUrl", "", "url", "app_debug"})
public final class ApiConfig {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "api_config";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_BASE_URL = "base_url";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DEFAULT_BASE_URL = "http://127.0.0.1:8000";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_TELEGRAM_BOT_USERNAME = "telegram_bot_username";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DEFAULT_TELEGRAM_BOT_USERNAME = "Message_first_bot";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ihatemylife.api.ApiConfig INSTANCE = null;
    
    private ApiConfig() {
        super();
    }
    
    /**
     * Get base URL from SharedPreferences or return default
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBaseUrl(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * Set base URL (for settings)
     */
    public final void setBaseUrl(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String url) {
    }
    
    /**
     * Telegram bot username for "Connect Telegram" (t.me/username).
     * Set via SharedPreferences or use default; used to open the bot so the user can link their account via the webhook.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTelegramBotUsername(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * URL to open the Telegram bot (Connect Telegram flow).
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTelegramConnectUrl(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
}