package com.example.ihatemylife.api.models

import com.google.gson.annotations.SerializedName

/**
 * API model for User matching backend schema (backend4).
 * Backend: POST /users/ (UserCreate), PATCH /users/{username}/telegram (link Telegram).
 * Response: UserOut (id, username, telegram_id optional).
 */
data class ApiUserCreate(
    @SerializedName("username")
    val username: String,
    
    @SerializedName("password")
    val password: String
)

data class ApiUserOut(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("telegram_id")
    val telegramId: Long? = null
)

/** Body for PATCH /users/{username}/telegram - link Telegram account to app user. */
data class ApiUserTelegramLink(
    @SerializedName("telegram_id")
    val telegramId: Long
)

