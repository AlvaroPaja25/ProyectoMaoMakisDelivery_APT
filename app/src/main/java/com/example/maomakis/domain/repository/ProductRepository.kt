package com.example.maomakis.domain.repository

import com.example.maomakis.domain.model.ProductListModel
import com.example.maomakis.domain.model.ProductRegisterModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<ProductListModel>>
    fun getProductsByCategory(categoryId: Int): Flow<List<ProductListModel>>

    fun getFavoriteProducts(): Flow<List<ProductListModel>>
    fun getProductById(productId: Int): Flow<ProductListModel?>
    suspend fun insert(product: ProductRegisterModel)
    suspend fun update(product: ProductRegisterModel)
    suspend fun delete(productId: Int)
    suspend fun setFavorite(productId: Int, favorite: Boolean)
}
