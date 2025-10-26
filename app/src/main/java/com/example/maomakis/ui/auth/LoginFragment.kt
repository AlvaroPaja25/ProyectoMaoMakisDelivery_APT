package com.example.maomakis.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentLoginBinding
import com.example.maomakis.domain.result.LoginResult
import com.example.maomakis.ui.MainActivity
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val authViewModel: AuthViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private lateinit var b: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b = FragmentLoginBinding.bind(view)

        setupObservers()

        b.buttonLogin.setOnClickListener {
            val email = b.editTextEmail.text.toString()
            val password = b.editTextPassword.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                authViewModel.login(email, password)
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        b.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            authViewModel.loginResult.collect { result ->
                when (result) {
                    is LoginResult.Success -> {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                    is LoginResult.Error -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                    }

                    null -> {
                        // Estado inicial, no hacer nada
                    }
                }
            }
        }
    }
}
