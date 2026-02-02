package com.example.ihatemylife.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ihatemylife.ChatActivity
import com.example.ihatemylife.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Firebase Cloud Messaging service for handling incoming message notifications
 */
class MessageNotificationService : FirebaseMessagingService() {
    
    companion object {
        private const val CHANNEL_ID = "messages_channel"
        private const val CHANNEL_NAME = "Messages"
        internal const val NOTIFICATION_ID_BASE = 1000
        
        /**
         * Create notification channel (required for Android 8.0+)
         */
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for incoming messages"
                    enableVibration(true)
                    enableLights(true)
                }
                
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
        
        /**
         * Check if chat is muted. Notifications are per-chat only (no global toggle).
         */
        private fun isChatMuted(context: Context, chatId: String): Boolean {
            val prefs = context.getSharedPreferences("chat_mutes", Context.MODE_PRIVATE)
            return prefs.getBoolean("chat_muted_$chatId", false)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Extract message data
        val data = remoteMessage.data
        val messageId = data["message_id"]?.toIntOrNull()
        val senderId = data["sender_id"]?.toIntOrNull()
        val senderUsername = data["sender_username"] ?: "Unknown"
        val content = data["content"] ?: "New message"
        val chatId = data["chat_id"] ?: "default"
        val chatTitle = data["chat_title"] ?: senderUsername
        
        // Check if chat is muted
        if (Companion.isChatMuted(this, chatId)) {
            return
        }
        
        // Create notification
        showNotification(
            messageId = messageId ?: 0,
            senderUsername = senderUsername,
            content = content,
            chatId = chatId,
            chatTitle = chatTitle
        )
    }
    
    private fun showNotification(
        messageId: Int,
        senderUsername: String,
        content: String,
        chatId: String,
        chatTitle: String
    ) {
        // Intent to open ChatActivity when notification is tapped
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("chat_id", chatId)
            putExtra("chat_title", chatTitle)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            messageId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Intent for "Mark as Read" action
        val readIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "MARK_AS_READ"
            putExtra("message_id", messageId)
            putExtra("chat_id", chatId)
        }
        
        val readPendingIntent = PendingIntent.getBroadcast(
            this,
            messageId * 2,
            readIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Build notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // You should replace this with your app icon
            .setContentTitle(chatTitle)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                android.R.drawable.ic_menu_view,
                "Mark as Read",
                readPendingIntent
            )
            .setGroup("messages_group")
            .setGroupSummary(false)
            .build()
        
        // Show notification
        val notificationManager = NotificationManagerCompat.from(this)
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(NOTIFICATION_ID_BASE + messageId, notification)
        }
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to backend server
        // TODO: Implement token registration with backend
    }
}
