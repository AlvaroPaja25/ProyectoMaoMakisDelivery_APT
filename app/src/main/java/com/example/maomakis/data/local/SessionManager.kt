package com.example.maomakis.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("MaoMakisPrefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "user_id"
    }

    /**
     * Guarda el ID del usuario que ha iniciado sesión.
     */
    fun saveAuthToken(userId: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, userId)
        editor.apply()
    }

    /**
     * Obtiene el ID del usuario que ha iniciado sesión.
     * Devuelve -1 si no hay nadie logueado.
     */
    fun getAuthToken(): Int {
        return prefs.getInt(USER_ID, -1)
    }

    /**
     * Cierra la sesión eliminando el ID del usuario.
     */
    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_ID)
        editor.apply()
    }
}
