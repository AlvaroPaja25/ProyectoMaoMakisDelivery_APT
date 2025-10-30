package com.example.maomakis.data.repository

import com.example.maomakis.data.local.dao.OrderDAO
import com.example.maomakis.data.local.entity.Order
import com.example.maomakis.data.local.entity.OrderItem
import com.example.maomakis.domain.model.CarritoModel
import com.example.maomakis.domain.repository.OrderRepository

class OrderRepositoryImpl(private val orderDAO: OrderDAO) : OrderRepository {
    override suspend fun placeOrder(
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
    ): Long {
        val order = Order(
            userId = userId,
            userName = userName,
            userEmail = userEmail,
            address = address,
            deliveryType = deliveryType,
            deliveryFee = deliveryFee,
            paymentMethod = paymentMethod,
            subtotal = subtotal,
            total = total,
            createdAt = createdAt
        )
        val orderItems = items.map { item ->
            OrderItem(
                orderId = 0,
                productId = item.productId,
                name = item.name,
                price = item.price,
                quantity = item.cant,
                lineTotal = item.subTotal
            )
        }
        return orderDAO.insertOrderWithItems(order, orderItems)
    }

    override fun getAllOrders() = orderDAO.getAllOrders()

    override suspend fun getItemsForOrder(orderId: Long) = orderDAO.getItemsForOrder(orderId)
}
