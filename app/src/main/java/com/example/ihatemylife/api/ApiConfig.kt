package com.example.ihatemylife.api

import android.content.Context

/**
 * API configuration - base URL for the backend (Interferention-compatible).
 * Default: http://127.0.0.1:8000 (FastAPI: users, messages, webhook/telegram).
 * Backend: POST /users/, PATCH /users/{username}/telegram; messages (send, sent, received, all, conversation, read, delivered).
 * No contacts API; contacts are stored locally only.
 */
object ApiConfig {
    private const val PREFS_NAME = "api_config"
    private const val KEY_BASE_URL = "base_url"
    private const val DEFAULT_BASE_URL = "http://127.0.0.1:8000"
    private const val KEY_TELEGRAM_BOT_USERNAME = "telegram_bot_username"
    private const val DEFAULT_TELEGRAM_BOT_USERNAME = "Message_first_bot"

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

    /**
     * Telegram bot username for "Connect Telegram" (t.me/username).
     * Set via SharedPreferences or use default; used to open the bot so the user can link their account via the webhook.
     */
    fun getTelegramBotUsername(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TELEGRAM_BOT_USERNAME, DEFAULT_TELEGRAM_BOT_USERNAME) ?: DEFAULT_TELEGRAM_BOT_USERNAME
    }

    /**
     * URL to open the Telegram bot (Connect Telegram flow).
     */
    fun getTelegramConnectUrl(context: Context): String =
        "https://t.me/${getTelegramBotUsername(context)}"
}

