package com.example.maomakis.ui.view.main.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.maomakis.R
import com.example.maomakis.databinding.ItemProductDetailBinding
import com.example.maomakis.domain.model.ProductListModel
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.CarritoViewModel
import com.example.maomakis.ui.viewmodel.ProductViewModel
import com.example.maomakis.ui.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class FavoriteProductDetailBottomSheet : BottomSheetDialogFragment() {
    private var _binding: ItemProductDetailBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private lateinit var carritoViewModel: CarritoViewModel
    private lateinit var productViewModel: ProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ItemProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory(requireActivity().application, this)
        carritoViewModel = ViewModelProvider(this, factory)[CarritoViewModel::class.java]
        productViewModel = ViewModelProvider(requireActivity(), factory)[ProductViewModel::class.java]

        val productId = arguments?.getInt(ARG_PRODUCT_ID, -1) ?: -1
        if (productId <= 0) {
            dismiss()
            return
        }

        // Observa cambios del producto
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                productViewModel.observeProductById(productId).collect { product ->
                    if (product != null) bindProduct(product)
                }
            }
        }

        binding.favoriteButton.setOnClickListener {
            val user = userViewModel.loggedInUser.value
            val product = productViewModel.products.value.firstOrNull { it.id == productId }
            if (user != null && product != null) {
                productViewModel.toggleFavorite(productId, product.favorite)
            } else {
                Toast.makeText(requireContext(), "Inicia sesión para usar favoritos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.addToCartButton.setOnClickListener {
            val user = userViewModel.loggedInUser.value
            if (user != null) {
                carritoViewModel.addProduct(user.id, productId)
                Toast.makeText(requireContext(), "Añadido al carrito", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Inicia sesión para añadir productos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindProduct(product: ProductListModel) {
        binding.detailTitle.text = product.name
        binding.detailPrice.text = getString(R.string.currency_format, product.price)
        binding.detailRating.text = product.rating.toString()
        binding.detailTiming.text = "10:00 - 23:00"

        product.iconResName?.let { binding.smallDetailImage.setImageResource(it) }

        val iconRes = if (product.favorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        binding.favoriteButton.setImageResource(iconRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PRODUCT_ID = "PRODUCT_ID"

        fun newInstance(productId: Int) = FavoriteProductDetailBottomSheet().apply {
            arguments = Bundle().apply { putInt(ARG_PRODUCT_ID, productId) }
        }
    }
}
