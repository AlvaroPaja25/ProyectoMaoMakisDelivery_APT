package com.example.maomakis.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {

    private lateinit var b: FragmentStartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b = FragmentStartBinding.bind(view)

        b.goToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_login)
        }

        b.buttonGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_register)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setTitle("Salir de la aplicación")
                .setMessage("¿Estás seguro de que deseas cerrar la app?")
                .setPositiveButton("Sí") { _, _ ->
                    requireActivity().finishAffinity() // Cierra toda la app
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}
