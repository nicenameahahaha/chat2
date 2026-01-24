package com.example.ihatemylife.api.models

import com.google.gson.annotations.SerializedName

/**
 * API model for User matching backend schema
 * Backend endpoint: POST /users/ (UserCreate: username, password)
 * Response: UserOut (id, username)
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
    val username: String
)

