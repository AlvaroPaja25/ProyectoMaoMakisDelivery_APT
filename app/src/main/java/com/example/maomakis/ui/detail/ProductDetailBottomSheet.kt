package com.example.maomakis.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.maomakis.R
import com.example.maomakis.databinding.ActivityProductDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.CarritoViewModel
import com.example.maomakis.ui.viewmodel.UserViewModel

class ProductDetailBottomSheet : BottomSheetDialogFragment() {
    private var _binding: ActivityProductDetailBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private lateinit var carritoViewModel: CarritoViewModel

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory(requireActivity().application, this)
        carritoViewModel = ViewModelProvider(this, factory)[CarritoViewModel::class.java]

        val args = arguments
        val productImageResId = args?.getInt("PRODUCT_IMAGE", R.drawable.dinner) ?: R.drawable.dinner
        val productName = args?.getString("PRODUCT_NAME") ?: "Pizza Error" // Default para debug
        val productPrice = args?.getString("PRODUCT_PRICE") ?: "$0.00"
        val productId = args?.getInt("PRODUCT_ID", -1) ?: -1

        binding.smallDetailImage.setImageResource(productImageResId)
        binding.detailTitle.text = productName
        binding.detailPrice.text = productPrice

        binding.detailRating.text = "5.0"
        binding.detailTiming.text = "10:00 - 23:00"

        binding.addToCartButton.setOnClickListener {
            val user = userViewModel.loggedInUser.value
            if (productId > 0 && user != null) {
                carritoViewModel.addProduct(user.id, productId)
                Toast.makeText(requireContext(), "$productName añadido al carrito", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Inicia sesión para añadir productos", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(name: String, price: String, imageResId: Int, productId: Int = -1) =
            ProductDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString("PRODUCT_NAME", name)
                    putString("PRODUCT_PRICE", price)
                    putInt("PRODUCT_IMAGE", imageResId)
                    putInt("PRODUCT_ID", productId)
                }
            }
    }
}