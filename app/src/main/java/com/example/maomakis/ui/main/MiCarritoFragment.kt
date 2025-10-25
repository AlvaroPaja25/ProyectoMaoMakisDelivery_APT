package com.example.maomakis.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentMiCarritoBinding
import com.example.maomakis.ui.adapter.MiCarritoAdapter
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.CarritoViewModel
import com.example.maomakis.ui.viewmodel.UserViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MiCarritoFragment : Fragment() {

    private var _binding: FragmentMiCarritoBinding? = null
    private val binding get() = _binding!!

    // ViewModel compartido para saber quién es el usuario
    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private val carritoViewModel: CarritoViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }

    private lateinit var miCarritoAdapter: MiCarritoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMiCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            // Esperamos a tener un usuario válido. filterNotNull().first() suspende hasta que haya un usuario.
            val user = userViewModel.loggedInUser.filterNotNull().first()

            // Ahora que tenemos un usuario y su ID, configuramos la UI
            setupRecyclerView(user.id)
            observeCartItems(user.id)
        }
    }

    private fun setupRecyclerView(userId: Int) {
        miCarritoAdapter = MiCarritoAdapter(
            onIncreaseClicked = { item ->
                carritoViewModel.updateQuantity(userId, item.productId, item.cant + 1)
            },
            onDecreaseClicked = { item ->
                if (item.cant > 1) {
                    carritoViewModel.updateQuantity(userId, item.productId, item.cant - 1)
                } else {
                    // Si la cantidad es 1, eliminamos el producto
                    carritoViewModel.removeProduct(userId, item.productId)
                }
            }
        )

        binding.cartItemsRecyclerView.apply {
            adapter = miCarritoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeCartItems(userId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            carritoViewModel.getCartItems(userId).collect { cartItems ->
                miCarritoAdapter.submitList(cartItems)

                val total = cartItems.sumOf { it.subTotal }
                binding.totalAmount.text = getString(R.string.currency_format, total)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
