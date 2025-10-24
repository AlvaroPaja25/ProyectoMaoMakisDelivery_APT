package com.example.maomakis.domain.model

data class CategoryRegisterModel(
    val id: Int,
    val name: String,
    val description: String?,
    val iconResName: String?
)

data class CategoryListModel(
    val id: Int,
    val name: String,
    val description: String?,
    val iconResName: Int?
)