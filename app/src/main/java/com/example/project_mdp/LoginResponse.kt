package com.example.project_mdp

data class LoginResponse(
    val msg: String,
    val user: UserInfo?
)

data class UserInfo(
    val id: String,
    val email: String,
    val namaLengkap: String
)