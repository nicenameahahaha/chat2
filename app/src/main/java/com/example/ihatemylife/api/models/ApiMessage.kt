package com.example.ihatemylife.api.models

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * API model for Message matching backend schema
 * Backend uses: sender_id, receiver_id (not chat_id)
 * Source enum: OWN_MESSENGER or TELEGRAM
 */
data class ApiMessageCreate(
    @SerializedName("content")
    val content: String,
    
    @SerializedName("receiver_username")
    val receiverUsername: String? = null,
    
    @SerializedName("reply_to_message_id")
    val replyToMessageId: Int? = null
)

data class ApiMessageOut(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("sender_id")
    val senderId: Int,
    
    @SerializedName("receiver_id")
    val receiverId: Int? = null,
    
    @SerializedName("content")
    val content: String,
    
    @SerializedName("timestamp")
    val timestamp: String, // ISO datetime string from backend
    
    @SerializedName("is_delivered")
    val isDelivered: Boolean = false,
    
    @SerializedName("is_read")
    val isRead: Boolean = false,
    
    @SerializedName("read_at")
    val readAt: String? = null,
    
    @SerializedName("source")
    val source: String = "own_messenger", // "own_messenger" or "telegram"
    
    @SerializedName("reply_to_message_id")
    val replyToMessageId: Int? = null
)

data class ApiMessageStatusUpdate(
    @SerializedName("is_delivered")
    val isDelivered: Boolean? = null,
    
    @SerializedName("is_read")
    val isRead: Boolean? = null
)

data class ApiAllMessagesResponse(
    @SerializedName("sent")
    val sent: List<ApiMessageOut>,
    
    @SerializedName("received")
    val received: List<ApiMessageOut>
)

