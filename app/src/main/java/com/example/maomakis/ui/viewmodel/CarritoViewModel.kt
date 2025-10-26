package com.example.maomakis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maomakis.domain.model.CarritoModel
import com.example.maomakis.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarritoViewModel(
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    /**
     * Flujo que emite la lista  de los productos por User desde el repositorio.
     * Se mantiene activo mientras la UI esté suscrita (con un timeout de 5s).
     * A diferencia del flujo de product este necesita un paramentro por es una fun
     */
    fun getCartItems(userId:Int): StateFlow<List<CarritoModel>> {
        val cartItems: StateFlow<List<CarritoModel>> = carritoRepository.getCartItems( userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        return cartItems
    }

    fun addProduct(userId: Int, productId: Int) {
        viewModelScope.launch {
            carritoRepository.addProductToCart(userId, productId)
        }
    }

    fun removeProduct(userId: Int, productId: Int) {
        viewModelScope.launch {
            carritoRepository.removeProductFromCart(userId, productId)
        }
    }

    fun updateQuantity(userId: Int, productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            carritoRepository.updateProductQuantity(userId, productId, newQuantity)
        }
    }
}
