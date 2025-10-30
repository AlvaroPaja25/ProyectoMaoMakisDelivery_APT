package com.example.maomakis.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentOrderConfirmationBinding
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.OrderViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrderConfirmationFragment : DialogFragment() {

    private var _binding: FragmentOrderConfirmationBinding? = null
    private val binding get() = _binding!!

    private val orderViewModel: OrderViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderId = arguments?.getLong("orderId") ?: 0L
        binding.title.text = getString(R.string.order_confirmation_title)
        binding.message.text = getString(R.string.order_confirmation_message, orderId)

        binding.btnViewOrders.setOnClickListener {
            findNavController().navigate(R.id.nav_orders)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(5000)
            if (isAdded) {
                findNavController().navigate(R.id.nav_home)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
