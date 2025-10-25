package com.example.maomakis.domain.repository

import kotlinx.coroutines.flow.Flow
import com.example.maomakis.domain.model.UserModel

interface UserRepository {
    fun getLoggedInUser(): Flow<UserModel?>
    fun logout()
}

