package com.example.maomakis.models

data class CarritoModel(
    val image: Int, // Usaremos Int para el ID del recurso drawable
    val name: String,
    val price: Double, // Cambiado a Double para cálculos
    val rating: String
)
