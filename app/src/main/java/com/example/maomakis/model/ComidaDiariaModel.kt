package com.example.maomakis.model

data class ComidaDiariaModel(
    //asdasdasd
    val imagen: Int,
    val nombre: String,
    val descuento: String,
    val descripcion: String,
    val tipo: String

){
    constructor(imagen: Int, nombre: String, descuento: String, descripcion: String)
            : this(imagen, nombre, descuento, descripcion, "")
}

