package com.example.maomakis.domain.model

data class CarritoModel(
    val productId: Int,
    val name: String,
    val price: Double,
    val cant: Int,
    val iconResName: Int?,
    val score: String
) {
    val subTotal: Double
        get() = price * cant
}
