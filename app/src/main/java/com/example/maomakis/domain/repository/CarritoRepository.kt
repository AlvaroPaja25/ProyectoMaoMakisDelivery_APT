package com.example.maomakis.domain.repository

import com.example.maomakis.domain.model.CarritoModel
import kotlinx.coroutines.flow.Flow

interface CarritoRepository {

    fun getCartItems(userId: Int): Flow<List<CarritoModel>>
    suspend fun addProductToCart(userId: Int, productId: Int)
    suspend fun removeProductFromCart(userId: Int, productId: Int)
    suspend fun updateProductQuantity(userId: Int, productId: Int, newQuantity: Int)

}
