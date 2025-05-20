package com.yandex.mobile_school.example.data.api

import com.yandex.mobile_school.example.data.model.LoginRequest
import com.yandex.mobile_school.example.data.model.LoginResponse
import com.yandex.mobile_school.example.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
    
    @GET("users/{userId}")
    suspend fun getUserDetails(@Path("userId") userId: String): User
    
    @GET("users/profile")
    suspend fun getUserProfile(): User
}
