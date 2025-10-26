package com.example.maomakis.data.repository

import android.content.Context
import com.example.maomakis.data.local.dao.CarritoDAO
import com.example.maomakis.data.local.entity.Carrito
import com.example.maomakis.data.mappers.toModel
import com.example.maomakis.domain.model.CarritoModel
import com.example.maomakis.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CarritoRepositoryImpl(
    private val dao: CarritoDAO,
    private val context: Context
) : CarritoRepository {

    override fun getCartItems(userId: Int): Flow<List<CarritoModel>> {
        return dao.getCartItemsForUser(userId).map { list ->
            list.map { it.toModel(context) }
        }
    }

    override suspend fun addProductToCart(userId: Int, productId: Int) {
        val currentItem = dao.getItem(userId, productId)
        val newQuantity = currentItem?.cant?.plus(1) ?: 1

        dao.upsertItem(Carrito(userId = userId, productId = productId, cant = newQuantity))
    }

    override suspend fun removeProductFromCart(userId: Int, productId: Int) {
        dao.deleteItem(userId, productId)
    }

    override suspend fun updateProductQuantity(userId: Int, productId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            // Si la nueva cantidad es 0 o menos, eliminamos el producto del carrito
            dao.deleteItem(userId, productId)
        } else {
            // Si no, actualizamos la cantidad
            dao.upsertItem(Carrito(userId = userId, productId = productId, cant = newQuantity))
        }
    }
}
