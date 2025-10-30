package com.example.maomakis.domain.repository

import com.example.maomakis.domain.model.CarritoModel

interface OrderRepository {
    suspend fun placeOrder(
        userId: Int,
        userName: String,
        userEmail: String,
        address: String,
        deliveryType: String,
        deliveryFee: Double,
        paymentMethod: String,
        subtotal: Double,
        total: Double,
        items: List<CarritoModel>,
        createdAt: Long
    ): Long

    fun getAllOrders(): kotlinx.coroutines.flow.Flow<List<com.example.maomakis.data.local.entity.Order>>
    suspend fun getItemsForOrder(orderId: Long): List<com.example.maomakis.data.local.entity.OrderItem>
}
