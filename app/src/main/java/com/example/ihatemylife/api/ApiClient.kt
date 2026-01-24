package com.example.ihatemylife.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit API client setup
 * Creates OkHttp client with logging and Retrofit instance
 */
object ApiClient {
    private var retrofit: Retrofit? = null
    private var apiService: ApiService? = null
    
    /**
     * Get Gson instance with custom date format handling
     */
    private fun getGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
    
    /**
     * Get OkHttp client with interceptors
     */
    private fun getOkHttpClient(context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Get or create Retrofit instance
     */
    fun getRetrofit(context: Context): Retrofit {
        if (retrofit == null) {
            val baseUrl = ApiConfig.getBaseUrl(context)
            
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl.ensureTrailingSlash())
                .client(getOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build()
        }
        return retrofit!!
    }
    
    /**
     * Get API service instance
     */
    fun getApiService(context: Context): ApiService {
        if (apiService == null) {
            apiService = getRetrofit(context).create(ApiService::class.java)
        }
        return apiService!!
    }
    
    /**
     * Reset client (useful when base URL changes)
     */
    fun reset() {
        retrofit = null
        apiService = null
    }
    
    private fun String.ensureTrailingSlash(): String {
        return if (this.endsWith("/")) this else "$this/"
    }
}

