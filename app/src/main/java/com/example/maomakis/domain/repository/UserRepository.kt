package com.example.maomakis.domain.repository

import kotlinx.coroutines.flow.Flow
import com.example.maomakis.domain.model.UserModel
import com.example.maomakis.domain.model.UserRegisterModel

interface UserRepository {
    suspend fun insertOrUpdate(user: UserRegisterModel)
    suspend fun delete(userId: Int)
    fun getUser(userId: Int): Flow<UserModel?>
    fun getAllUsers(): Flow<List<UserModel>>
}