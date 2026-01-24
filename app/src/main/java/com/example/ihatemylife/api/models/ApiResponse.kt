package com.example.ihatemylife.api.models

/**
 * Generic API response wrapper
 */
data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
)

