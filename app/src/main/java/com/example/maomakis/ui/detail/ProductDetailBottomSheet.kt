package com.example.maomakis.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.maomakis.R
import com.example.maomakis.databinding.ActivityProductDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProductDetailBottomSheet : BottomSheetDialogFragment() {
    private var _binding: ActivityProductDetailBinding? = null
    private val binding get() = _binding!!

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

        val args = arguments
        val productImageResId = args?.getInt("PRODUCT_IMAGE", R.drawable.dinner) ?: R.drawable.dinner
        val productName = args?.getString("PRODUCT_NAME") ?: "Pizza Error" // Default para debug
        val productPrice = args?.getString("PRODUCT_PRICE") ?: "$0.00"

        binding.smallDetailImage.setImageResource(productImageResId)
        binding.detailTitle.text = productName
        binding.detailPrice.text = productPrice

        binding.detailRating.text = "5.0"
        binding.detailTiming.text = "10:00 - 23:00"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(name: String, price: String, imageResId: Int) =
            ProductDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString("PRODUCT_NAME", name)
                    putString("PRODUCT_PRICE", price)
                    putInt("PRODUCT_IMAGE", imageResId)
                }
            }
    }
}