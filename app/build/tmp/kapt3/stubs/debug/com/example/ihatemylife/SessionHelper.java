package com.example.ihatemylife;

/**
 * Centralized session and logout logic.
 * Clears all stored authentication data and navigates to RegisterActivity
 * with a cleared back stack so the user cannot return to authenticated screens.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/example/ihatemylife/SessionHelper;", "", "()V", "USER_PREFS_NAME", "", "clearSession", "", "context", "Landroid/content/Context;", "logoutAndNavigateToRegister", "app_debug"})
public final class SessionHelper {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String USER_PREFS_NAME = "user_prefs";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ihatemylife.SessionHelper INSTANCE = null;
    
    private SessionHelper() {
        super();
    }
    
    /**
     * Clears login state and all stored authentication data:
     * - logged_in flag
     * - user_identifier (email)
     * - username
     * - user_id (backend id)
     * - avatar_path, phone (profile data tied to session)
     */
    public final void clearSession(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * Performs full logout: clears session data and navigates to RegisterActivity.
     * Clears the back stack so the user cannot return to authenticated screens.
     * Call from an Activity context so that finish() can be invoked.
     */
    public final void logoutAndNavigateToRegister(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
}