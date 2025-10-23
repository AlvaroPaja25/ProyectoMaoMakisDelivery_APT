package com.example.maomakis.data.repository

import com.example.maomakis.data.local.dao.UserDAO
import com.example.maomakis.domain.model.UserModel
import com.example.maomakis.domain.repository.UserRepository
import com.example.maomakis.domain.model.UserRegisterModel
import com.example.maomakis.data.mappers.toEntity
import com.example.maomakis.data.mappers.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserRepositoryImpl(
    private val dao: UserDAO
) : UserRepository {
    override suspend fun insertOrUpdate(user: UserRegisterModel) {
        dao.insert(user.toEntity())
    }

    override suspend fun delete(userId: Int) {
        dao.delete(userId)
    }

    override fun getUser(userId: Int): Flow<UserModel?> =
        dao.getUserById(userId).map { it?.toModel() }

    override fun getAllUsers(): Flow<List<UserModel>> =
        dao.getAllUsers().map { list -> list.map { it.toModel() } }
}