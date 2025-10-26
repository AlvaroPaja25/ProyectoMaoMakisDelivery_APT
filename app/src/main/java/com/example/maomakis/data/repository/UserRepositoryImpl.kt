package com.example.maomakis.data.repository

import com.example.maomakis.data.local.SessionManager
import com.example.maomakis.data.local.dao.UserDAO
import com.example.maomakis.data.mappers.toModel
import com.example.maomakis.domain.model.UserModel
import com.example.maomakis.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val dao: UserDAO,
    private val sessionManager: SessionManager
) : UserRepository {

    override fun getLoggedInUser(): Flow<UserModel?> {
        val userId = sessionManager.getAuthToken()
        return if (userId != -1) {
            dao.getUserById(userId).map { it?.toModel() }
        } else {
            flowOf(null)
        }
    }

    override fun logout() {
        sessionManager.clearAuthToken()
    }
}
