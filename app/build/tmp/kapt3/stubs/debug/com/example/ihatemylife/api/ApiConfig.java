package com.example.ihatemylife.api;

/**
 * API configuration - base URL and settings
 * Default: http://127.0.0.1:8000 (localhost for development)
 * Can be configured in app settings
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tJ\u0016\u0010\n\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/example/ihatemylife/api/ApiConfig;", "", "()V", "DEFAULT_BASE_URL", "", "KEY_BASE_URL", "PREFS_NAME", "getBaseUrl", "context", "Landroid/content/Context;", "setBaseUrl", "", "url", "app_debug"})
public final class ApiConfig {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "api_config";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_BASE_URL = "base_url";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DEFAULT_BASE_URL = "http://127.0.0.1:8000";
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
}