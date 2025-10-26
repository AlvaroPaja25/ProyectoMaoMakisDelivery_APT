package com.example.maomakis.domain.repository

import com.example.maomakis.domain.model.UserRegisterModel
import com.example.maomakis.domain.result.LoginResult
import com.example.maomakis.domain.result.RegisterResult


interface AuthRepository {
    suspend fun login(email: String, password: String): LoginResult
    suspend fun register(user: UserRegisterModel): RegisterResult
    fun isUserLoggedIn(): Boolean
}
