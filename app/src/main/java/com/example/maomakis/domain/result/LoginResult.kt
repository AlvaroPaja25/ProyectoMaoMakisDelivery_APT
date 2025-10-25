package com.example.maomakis.domain.result

import com.example.maomakis.domain.model.UserModel

sealed class LoginResult {
    data class Success(val user: UserModel) : LoginResult()
    data class Error(val message: String) : LoginResult()
}
