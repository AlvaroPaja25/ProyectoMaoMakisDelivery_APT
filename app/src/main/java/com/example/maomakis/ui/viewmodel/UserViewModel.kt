package com.example.maomakis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maomakis.domain.model.UserModel
import com.example.maomakis.domain.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel para gestionar el estado del usuario logueado.
 * Es compartido a través de la app.
 * Depende de la interfaz UserRepository.
 */
class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    /**
     * Un flujo que emite el UserModel del usuario logueado, o null si no hay sesión.
     */
    val loggedInUser: StateFlow<UserModel?> = userRepository.getLoggedInUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun logout() {
        userRepository.logout()
        // El flujo loggedInUser se actualizará a null automáticamente
    }
}
