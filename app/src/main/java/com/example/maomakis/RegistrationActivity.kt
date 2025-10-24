package com.example.maomakis

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.maomakis.databinding.ActivityRegistrationBinding
import com.example.maomakis.domain.repository.RegisterResult
import com.example.maomakis.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()

        binding.buttonRegister.setOnClickListener { // Asumiendo que el botón de registro se llama 'button'
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                viewModel.register(name, email, password)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.goToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.registerResult.collect { result ->
                when (result) {
                    is RegisterResult.Success -> {
                        Toast.makeText(this@RegistrationActivity, "¡Registro exitoso! Por favor, inicie sesión.", Toast.LENGTH_LONG).show()
                        // Navegamos a LoginActivity después de un registro exitoso
                        val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    is RegisterResult.Error -> {
                        Toast.makeText(this@RegistrationActivity, result.message, Toast.LENGTH_LONG).show()
                    }
                    null -> {
                        // Estado inicial
                    }
                }
            }
        }
    }
}
