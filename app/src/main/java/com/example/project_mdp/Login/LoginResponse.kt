package com.example.project_mdp.Login

data class LoginResponse(
    val message: String,
    val token: String?,
    val user: User?
)