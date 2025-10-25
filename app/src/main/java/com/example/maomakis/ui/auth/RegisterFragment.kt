package com.example.maomakis.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentRegisterBinding
import com.example.maomakis.domain.result.RegisterResult
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val authViewModel: AuthViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private lateinit var b: FragmentRegisterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b = FragmentRegisterBinding.bind(view)

        setupObservers()

        b.buttonRegister.setOnClickListener {
            val name = b.editTextName.text.toString()
            val email = b.editTextEmail.text.toString()
            val password = b.editTextPassword.text.toString()

            if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                authViewModel.register(name, email, password)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Por favor, complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        b.goToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            authViewModel.registerResult.collect { result ->
                when (result) {
                    is RegisterResult.Success -> {
                        Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    is RegisterResult.Error -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                    }
                    null -> {
                        // Estado inicial
                    }
                }
            }
        }
    }
}