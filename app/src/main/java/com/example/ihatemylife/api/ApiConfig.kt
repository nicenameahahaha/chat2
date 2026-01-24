package com.example.ihatemylife.api

import android.content.Context
import android.content.SharedPreferences

/**
 * API configuration - base URL and settings
 * Default: http://127.0.0.1:8000 (localhost for development)
 * Can be configured in app settings
 */
object ApiConfig {
    private const val PREFS_NAME = "api_config"
    private const val KEY_BASE_URL = "base_url"
    private const val DEFAULT_BASE_URL = "http://127.0.0.1:8000"
    
    /**
     * Get base URL from SharedPreferences or return default
     */
    fun getBaseUrl(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_BASE_URL, DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL
    }
    
    /**
     * Set base URL (for settings)
     */
    fun setBaseUrl(context: Context, url: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_BASE_URL, url).apply()
    }
}

