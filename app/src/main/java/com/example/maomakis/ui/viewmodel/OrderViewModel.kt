package com.example.maomakis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maomakis.domain.model.CarritoModel
import com.example.maomakis.domain.repository.CarritoRepository
import com.example.maomakis.domain.repository.OrderRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import com.example.maomakis.data.local.entity.Order
import com.example.maomakis.data.local.entity.OrderItem

class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    fun getAllOrders(): Flow<List<Order>> = orderRepository.getAllOrders()

    suspend fun getItemsForOrder(orderId: Long): List<OrderItem> = orderRepository.getItemsForOrder(orderId)

    fun placeOrderAndClearCart(
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
        onDone: (Long) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val orderId = orderRepository.placeOrder(
                    userId, userName, userEmail, address,
                    deliveryType, deliveryFee, paymentMethod,
                    subtotal, total, items, System.currentTimeMillis()
                )
                carritoRepository.clearCart(userId)
                onDone(orderId)
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}
