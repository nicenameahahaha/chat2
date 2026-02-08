package com.example.ihatemylife.repository

import android.content.Context
import com.example.ihatemylife.api.ApiClient
import com.example.ihatemylife.api.models.ApiUserCreate
import com.example.ihatemylife.api.models.ApiUserOut
import com.example.ihatemylife.database.AppDatabase
import com.example.ihatemylife.database.dao.UserDao
import com.example.ihatemylife.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for user operations
 * Combines local (Room) and remote (API) data sources
 */
class UserRepository(context: Context) {
    private val apiService = ApiClient.getApiService(context)
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()
    
    /**
     * Register a new user via API
     */
    suspend fun registerUser(username: String, password: String): Result<ApiUserOut> {
        return try {
            val response = apiService.registerUser(ApiUserCreate(username, password))
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                // Save to local database
                userDao.insertUser(UserEntity(user.id, user.username))
                Result.success(user)
            } else {
                Result.failure(Exception(response.message() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get user by username from local database
     */
    suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getUserByUsername(username)
    }
    
    /**
     * Get user by username as Flow
     */
    fun getUserByUsernameFlow(username: String): Flow<UserEntity?> {
        return userDao.getUserByUsernameFlow(username)
    }
    
    /**
     * Get all users from local database
     */
    fun getAllUsersFlow(): Flow<List<UserEntity>> {
        return userDao.getAllUsersFlow()
    }
    
    /**
     * Sync users from API to local database
     */
    suspend fun syncUsers(): Result<Unit> {
        // Note: Backend doesn't have a "get all users" endpoint
        // This would need to be implemented or users are created on registration
        return Result.success(Unit)
    }

    /**
     * Delete all users from the local database.
     */
    suspend fun clearAllUsers() {
        userDao.deleteAllUsers()
    }
}

