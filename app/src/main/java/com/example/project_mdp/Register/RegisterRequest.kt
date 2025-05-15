package com.example.project_mdp.Register

data class RegisterRequest(
    val namaLengkap: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
