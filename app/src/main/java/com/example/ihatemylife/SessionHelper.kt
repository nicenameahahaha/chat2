package com.example.ihatemylife

import android.content.Context
import android.content.Intent
import android.app.Activity

/**
 * Centralized session and logout logic.
 * Clears all stored authentication data and navigates to RegisterActivity
 * with a cleared back stack so the user cannot return to authenticated screens.
 */
object SessionHelper {

    private const val USER_PREFS_NAME = "user_prefs"

    /**
     * Clears login state and all stored authentication data:
     * - logged_in flag
     * - user_identifier (email)
     * - username
     * - user_id (backend id)
     * - avatar_path, phone (profile data tied to session)
     */
    fun clearSession(context: Context) {
        context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    /**
     * Performs full logout: clears session data and navigates to RegisterActivity.
     * Clears the back stack so the user cannot return to authenticated screens.
     * Call from an Activity context so that finish() can be invoked.
     */
    fun logoutAndNavigateToRegister(context: Context) {
        clearSession(context)
        val intent = Intent(context, RegisterActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
        (context as? Activity)?.finish()
    }
}
