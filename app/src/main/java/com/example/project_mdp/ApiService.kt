package com.example.project_mdp

import com.example.project_mdp.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/register")
    fun registerUser(@Body request: RegisterRequest): Call<Void>
    @POST("/api/auth/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

}