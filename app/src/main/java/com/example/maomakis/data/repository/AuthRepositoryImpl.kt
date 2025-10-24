package com.example.maomakis.data.repository

import com.example.maomakis.data.local.SessionManager
import com.example.maomakis.data.local.dao.UserDAO
import com.example.maomakis.data.local.entity.User
import com.example.maomakis.data.mappers.toModel
import com.example.maomakis.data.security.PasswordHasher
import com.example.maomakis.domain.model.UserRegisterModel
import com.example.maomakis.domain.repository.AuthRepository
import com.example.maomakis.domain.repository.LoginResult
import com.example.maomakis.domain.repository.RegisterResult

class AuthRepositoryImpl(
    private val userDAO: UserDAO,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): LoginResult {
        val user = userDAO.getUserByEmail(email)
        val hashedPassword = PasswordHasher.hashPassword(password)

        return if (user != null && user.password == hashedPassword) {
            sessionManager.saveAuthToken(user.id)
            LoginResult.Success(user.toModel())
        } else {
            LoginResult.Error("Las credenciales son incorrectas.")
        }
    }

    override suspend fun register(user: UserRegisterModel): RegisterResult {

        if (userDAO.getUserByEmail(user.email) != null) {
            return RegisterResult.Error("El correo electrónico ya está en uso.")
        }

        val hashedPassword = PasswordHasher.hashPassword(user.password)

        val newUser = User(
            name = user.name,
            email = user.email,
            password = hashedPassword
        )

        userDAO.insert(newUser)
        return RegisterResult.Success
    }

    override fun logout() {
        sessionManager.clearAuthToken()
    }
}
