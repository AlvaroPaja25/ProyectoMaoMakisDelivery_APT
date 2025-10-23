package com.example.maomakis.domain.repository

import com.example.maomakis.domain.model.UserModel
import com.example.maomakis.domain.model.UserRegisterModel

sealed class LoginResult {
    data class Success(val user: UserModel) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

sealed class RegisterResult {
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}

interface AuthRepository {
    /**
     * Intenta iniciar sesión con un email y una contraseña.
     * @return [LoginResult.Success] si las credenciales son correctas.
     * @return [LoginResult.Error] si las credenciales son incorrectas o el usuario no existe.
     */
    suspend fun login(email: String, password: String): LoginResult

    /**
     * Registra un nuevo usuario.
     * @return [RegisterResult.Success] si el registro es exitoso.
     * @return [RegisterResult.Error] si el email ya está en uso.
     */
    suspend fun register(user: UserRegisterModel): RegisterResult

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout()
}
