package com.example.ihatemylife.api.models

import com.google.gson.annotations.SerializedName

/**
 * API model for Contact matching backend schema
 * Backend endpoint: POST /contacts/ (ContactCreate: firstName, lastName, email?, phone?, userId)
 * Response: ContactOut (id, firstName, lastName, email?, phone?, userId)
 */
data class ApiContactCreate(
    @SerializedName("first_name")
    val firstName: String,
    
    @SerializedName("last_name")
    val lastName: String = "",
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("user_id")
    val userId: Int
)

data class ApiContactOut(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("first_name")
    val firstName: String,
    
    @SerializedName("last_name")
    val lastName: String = "",
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("user_id")
    val userId: Int
)
