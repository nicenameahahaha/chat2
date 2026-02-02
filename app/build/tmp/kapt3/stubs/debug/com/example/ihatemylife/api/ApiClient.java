package com.example.ihatemylife.api;

/**
 * Retrofit API client setup
 * Creates OkHttp client with logging and Retrofit instance
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tJ\b\u0010\n\u001a\u00020\u000bH\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\b\u001a\u00020\tH\u0002J\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tJ\u0006\u0010\u000f\u001a\u00020\u0010J\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0012H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/example/ihatemylife/api/ApiClient;", "", "()V", "apiService", "Lcom/example/ihatemylife/api/ApiService;", "retrofit", "Lretrofit2/Retrofit;", "getApiService", "context", "Landroid/content/Context;", "getGson", "Lcom/google/gson/Gson;", "getOkHttpClient", "Lokhttp3/OkHttpClient;", "getRetrofit", "reset", "", "ensureTrailingSlash", "", "app_debug"})
public final class ApiClient {
    @org.jetbrains.annotations.Nullable()
    private static retrofit2.Retrofit retrofit;
    @org.jetbrains.annotations.Nullable()
    private static com.example.ihatemylife.api.ApiService apiService;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ihatemylife.api.ApiClient INSTANCE = null;
    
    private ApiClient() {
        super();
    }
    
    /**
     * Get Gson instance with custom date format handling
     */
    private final com.google.gson.Gson getGson() {
        return null;
    }
    
    /**
     * Get OkHttp client with interceptors
     */
    private final okhttp3.OkHttpClient getOkHttpClient(android.content.Context context) {
        return null;
    }
    
    /**
     * Get or create Retrofit instance
     */
    @org.jetbrains.annotations.NotNull()
    public final retrofit2.Retrofit getRetrofit(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * Get API service instance
     */
    @org.jetbrains.annotations.NotNull()
    public final com.example.ihatemylife.api.ApiService getApiService(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * Reset client (useful when base URL changes)
     */
    public final void reset() {
    }
    
    private final java.lang.String ensureTrailingSlash(java.lang.String $this$ensureTrailingSlash) {
        return null;
    }
}