package com.example.maomakis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maomakis.domain.model.UserRegisterModel
import com.example.maomakis.domain.repository.AuthRepository
import com.example.maomakis.domain.result.LoginResult
import com.example.maomakis.domain.result.RegisterResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult: StateFlow<RegisterResult?> = _registerResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loginResult.value = result
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val user = UserRegisterModel(name, email, password)
            val result = authRepository.register(user)
            _registerResult.value = result
        }
    }
    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}
