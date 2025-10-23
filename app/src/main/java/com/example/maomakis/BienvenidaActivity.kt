package com.example.maomakis

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.maomakis.databinding.ActivityBienvenidaBinding
import com.example.maomakis.ui.viewmodel.UserViewModel

class BienvenidaActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityBienvenidaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Comprobamos si el usuario ya ha iniciado sesión
        if (userViewModel.isUserLoggedIn()) {
            // Si ya hay sesión, vamos directamente a MainActivity
            goToMainActivity()
            return // Detenemos la ejecución de onCreate aquí
        }

        // Si no hay sesión, mostramos la pantalla de bienvenida
        binding = ActivityBienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGoToRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.goToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Cerramos esta actividad para que no se pueda volver a ella
    }
}
