package com.example.maomakis.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maomakis.data.local.AppDatabase
import com.example.maomakis.data.local.SessionManager
import com.example.maomakis.data.mappers.toModel
import com.example.maomakis.domain.model.UserModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val userDAO = AppDatabase.getInstance(application).userDao()

    /**
     * Un flujo que emite el UserModel del usuario logueado, o null si no hay sesión.
     * Cualquier fragmento puede observar este flujo para obtener los datos del usuario.
     */
    val loggedInUser: StateFlow<UserModel?> = userDAO.getUserById(sessionManager.getAuthToken())
        .map { userEntity -> userEntity?.toModel() }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    /**
     * Comprueba si hay un usuario actualmente logueado.
     */
    fun isUserLoggedIn(): Boolean {
        return sessionManager.getAuthToken() != -1
    }

    /**
     * Cierra la sesión del usuario.
     */
    fun logout() {
        sessionManager.clearAuthToken()
        // Esto provocará que el flujo loggedInUser emita null automáticamente
    }
}
