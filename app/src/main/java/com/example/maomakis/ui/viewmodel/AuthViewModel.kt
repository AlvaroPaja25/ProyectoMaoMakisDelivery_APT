package com.example.maomakis.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maomakis.data.local.AppDatabase
import com.example.maomakis.data.local.SessionManager
import com.example.maomakis.data.repository.AuthRepositoryImpl
import com.example.maomakis.domain.model.UserRegisterModel
import com.example.maomakis.domain.repository.AuthRepository
import com.example.maomakis.domain.repository.LoginResult
import com.example.maomakis.domain.repository.RegisterResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult: StateFlow<RegisterResult?> = _registerResult

    init {
        val userDAO = AppDatabase.getInstance(application).userDao()
        val sessionManager = SessionManager(application)
        authRepository = AuthRepositoryImpl(userDAO, sessionManager)
    }

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
}
