package com.example.maomakis.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("MaoMakisPrefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "user_id"
    }

    fun saveAuthToken(userId: Int) {
        prefs.edit {
            putInt(USER_ID, userId)
        }
    }

    fun getAuthToken(): Int {
        return prefs.getInt(USER_ID, -1)
    }

    fun clearAuthToken() {
        prefs.edit {
            remove(USER_ID)
        }
    }
}
