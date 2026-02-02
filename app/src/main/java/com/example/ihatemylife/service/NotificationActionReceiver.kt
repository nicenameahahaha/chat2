package com.example.ihatemylife.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.ihatemylife.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Broadcast receiver for notification action buttons
 */
class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "MARK_AS_READ" -> {
                val messageId = intent.getIntExtra("message_id", -1)
                val chatId = intent.getStringExtra("chat_id")
                
                if (messageId > 0) {
                    // Get current username from preferences
                    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    val username = prefs.getString("username", "") ?: ""
                    
                    if (username.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val messageRepository = MessageRepository(context)
                            messageRepository.markAsRead(messageId, username)
                        }
                    }
                }
                
                // Dismiss notification
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                notificationManager.cancel(MessageNotificationService.NOTIFICATION_ID_BASE + messageId)
            }
        }
    }
}
