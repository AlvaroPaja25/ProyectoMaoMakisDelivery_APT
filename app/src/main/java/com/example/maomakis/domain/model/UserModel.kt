package com.example.maomakis.domain.model

data class UserModel(
    val id: Int,
    val displayName: String,
    val email: String
)

data class UserRegisterModel(
    val name: String,
    val email: String,
    val password: String
)
