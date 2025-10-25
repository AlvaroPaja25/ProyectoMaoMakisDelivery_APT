package com.example.maomakis.domain.model

data class ProductRegisterModel(
    val id: Int,
    val categoryId: Int,
    val favorite: Boolean,
    val score: String,
    val name: String,
    val description: String?,
    val iconResName: String?,
    val price: Double
)

data class ProductListModel(
    val id: Int,
    val category: String?,
    val favorite: Boolean,
    val score: String,
    val name: String,
    val description: String?,
    val iconResName: Int?,
    val price: Double
)
