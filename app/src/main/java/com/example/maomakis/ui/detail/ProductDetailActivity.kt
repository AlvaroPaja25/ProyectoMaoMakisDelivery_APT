package com.example.maomakis.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.maomakis.R
import com.example.maomakis.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productImageResId = intent.getIntExtra("PRODUCT_IMAGE", R.drawable.dinner)

        val productName = intent.getStringExtra("PRODUCT_NAME") ?: "Product"
        val productPrice = intent.getStringExtra("PRODUCT_PRICE") ?: "$0.00"

        binding.smallDetailImage.setImageResource(productImageResId)

        binding.detailTitle.text = productName
        binding.detailPrice.text = productPrice

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}