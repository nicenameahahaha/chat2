package com.example.ihatemylife.api

import com.example.ihatemylife.api.models.ApiMessageCreate
import com.example.ihatemylife.api.models.ApiMessageOut
import com.example.ihatemylife.api.models.ApiMessageStatusUpdate
import com.example.ihatemylife.api.models.ApiAllMessagesResponse
import com.example.ihatemylife.api.models.ApiUserCreate
import com.example.ihatemylife.api.models.ApiUserOut
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit interface for backend API endpoints
 * Matches FastAPI backend structure
 */
interface ApiService {
    
    // User endpoints
    @POST("users/")
    suspend fun registerUser(@Body user: ApiUserCreate): Response<ApiUserOut>
    
    // Message endpoints
    @POST("messages/")
    suspend fun sendMessage(@Body message: ApiMessageCreate): Response<ApiMessageOut>
    
    @POST("messages/send/{sender_username}/{receiver_username}")
    suspend fun sendMessageToUser(
        @Path("sender_username") senderUsername: String,
        @Path("receiver_username") receiverUsername: String,
        @Body message: ApiMessageCreate
    ): Response<ApiMessageOut>
    
    @GET("messages/sent/{username}")
    suspend fun getSentMessages(@Path("username") username: String): Response<List<ApiMessageOut>>
    
    @GET("messages/received/{username}")
    suspend fun getReceivedMessages(@Path("username") username: String): Response<List<ApiMessageOut>>
    
    @GET("messages/all/{username}")
    suspend fun getAllMessages(@Path("username") username: String): Response<ApiAllMessagesResponse>
    
    @GET("messages/conversation/{username1}/{username2}")
    suspend fun getConversation(
        @Path("username1") username1: String,
        @Path("username2") username2: String
    ): Response<List<ApiMessageOut>>
    
    @GET("messages/{message_id}")
    suspend fun getMessage(@Path("message_id") messageId: Int): Response<ApiMessageOut>
    
    @PATCH("messages/{message_id}/read/{username}")
    suspend fun markMessageAsRead(
        @Path("message_id") messageId: Int,
        @Path("username") username: String
    ): Response<ApiMessageOut>
    
    @PATCH("messages/{message_id}/delivered")
    suspend fun markMessageAsDelivered(@Path("message_id") messageId: Int): Response<ApiMessageOut>
}

