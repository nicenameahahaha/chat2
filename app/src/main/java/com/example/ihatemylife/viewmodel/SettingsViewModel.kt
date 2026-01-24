package com.example.ihatemylife.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for SettingsActivity
 * Manages app settings: theme, filters, notifications
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs: SharedPreferences = application.getSharedPreferences("app_settings", 0)
    
    // Theme settings
    private val _isDarkTheme = MutableStateFlow(prefs.getBoolean("dark_theme", false))
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    // Message filter settings
    private val _timeFilter = MutableStateFlow(prefs.getString("time_filter", "all") ?: "all")
    val timeFilter: StateFlow<String> = _timeFilter.asStateFlow()
    
    private val _messengerTypeFilter = MutableStateFlow(prefs.getString("messenger_filter", "all") ?: "all")
    val messengerTypeFilter: StateFlow<String> = _messengerTypeFilter.asStateFlow()
    
    private val _messageSizeFilter = MutableStateFlow(prefs.getString("message_size_filter", "all") ?: "all")
    val messageSizeFilter: StateFlow<String> = _messageSizeFilter.asStateFlow()
    
    // Notification settings
    private val _notificationsEnabled = MutableStateFlow(prefs.getBoolean("notifications_enabled", true))
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()
    
    /**
     * Set dark theme preference
     */
    fun setDarkTheme(enabled: Boolean) {
        _isDarkTheme.value = enabled
        prefs.edit().putBoolean("dark_theme", enabled).apply()
    }
    
    /**
     * Set time filter
     */
    fun setTimeFilter(filter: String) {
        _timeFilter.value = filter
        prefs.edit().putString("time_filter", filter).apply()
    }
    
    /**
     * Set messenger type filter
     */
    fun setMessengerTypeFilter(filter: String) {
        _messengerTypeFilter.value = filter
        prefs.edit().putString("messenger_filter", filter).apply()
    }
    
    /**
     * Set message size filter
     */
    fun setMessageSizeFilter(filter: String) {
        _messageSizeFilter.value = filter
        prefs.edit().putString("message_size_filter", filter).apply()
    }
    
    /**
     * Set notifications enabled
     */
    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }
}

