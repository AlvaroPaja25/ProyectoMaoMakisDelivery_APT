package com.example.maomakis.ui.view.main.detail

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.maomakis.R
import com.example.maomakis.databinding.ActivityProductDetailBinding
import com.example.maomakis.domain.model.ProductListModel
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.CarritoViewModel
import com.example.maomakis.ui.viewmodel.ProductViewModel
import com.example.maomakis.ui.viewmodel.UserViewModel

class ProductDetailBottomSheet : BottomSheetDialogFragment() {
    private var _binding: ActivityProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var carritoViewModel: CarritoViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel

    private var productId = 0
    private var productName = ""
    private var productPrice = ""
    private var productImage = 0
    private var isFavorite = false

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory(requireActivity().application, this)
        carritoViewModel = ViewModelProvider(this, factory)[CarritoViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity(), factory)[UserViewModel::class.java]
        productViewModel = ViewModelProvider(requireActivity(), factory)[ProductViewModel::class.java]

        arguments?.let {
            productId = it.getInt("PRODUCT_ID")
            productName = it.getString("PRODUCT_NAME") ?: ""
            productPrice = it.getString("PRODUCT_PRICE") ?: ""
            productImage = it.getInt("PRODUCT_IMAGE")
            isFavorite = it.getBoolean("PRODUCT_FAVORITE", false)
        }

        binding.smallDetailImage.setImageResource(productImage)
        binding.detailTitle.text = productName
        binding.detailPrice.text = productPrice

        // --- Botón de favorito ---
        updateFavoriteIcon()
        binding.favoriteButton.setOnClickListener {
            val user = userViewModel.loggedInUser.value
            if (user != null) {
                isFavorite = !isFavorite
                updateFavoriteIcon()
                productViewModel.toggleFavorite(user.id, productId)
            } else {
                Toast.makeText(requireContext(), "Inicia sesión para usar favoritos", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Botón de agregar al carrito ---
        binding.addToCartButton.setOnClickListener {
            val user = userViewModel.loggedInUser.value
            if (user != null) {
                carritoViewModel.addProduct(user.id, productId)
                Toast.makeText(requireContext(), "$productName añadido al carrito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Inicia sesión para añadir productos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFavoriteIcon() {
        val iconRes = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        binding.favoriteButton.setImageResource(iconRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(product: ProductListModel) = ProductDetailBottomSheet().apply {
            arguments = Bundle().apply {
                putInt("PRODUCT_ID", product.id)
                putString("PRODUCT_NAME", product.name)
                putString("PRODUCT_PRICE", product.price)
                putInt("PRODUCT_IMAGE", product.image)
                putBoolean("PRODUCT_FAVORITE", product.isFavorite)
            }
        }
    }
}
