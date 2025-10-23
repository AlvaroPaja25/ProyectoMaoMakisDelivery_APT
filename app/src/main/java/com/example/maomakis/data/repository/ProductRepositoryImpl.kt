package com.example.maomakis.data.repository

import android.content.Context
import com.example.maomakis.data.local.dao.ProductDAO
import com.example.maomakis.data.mappers.toEntity
import com.example.maomakis.data.mappers.toListModel
import com.example.maomakis.domain.model.ProductListModel
import com.example.maomakis.domain.model.ProductRegisterModel
import com.example.maomakis.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val dao: ProductDAO,
    private val context: Context
) : ProductRepository {

    override fun getAllProducts(): Flow<List<ProductListModel>> =
        dao.getAll().map { list -> list.map { it.toListModel(context) } }

    override fun getProductsByCategory(categoryId: Int): Flow<List<ProductListModel>> =
        dao.getByCategory(categoryId).map { list -> list.map { it.toListModel(context) } }

    override fun getFavoriteProducts(): Flow<List<ProductListModel>> =
        dao.getFavorites().map { list -> list.map { it.toListModel(context) } }

    override fun getProductById(productId: Int): Flow<ProductListModel?> =
        dao.getById(productId).map { it?.toListModel(context) }

    override suspend fun insert(product: ProductRegisterModel) {
        dao.insert(product.toEntity())
    }

    override suspend fun update(product: ProductRegisterModel) {
        dao.update(product.toEntity())
    }

    override suspend fun delete(productId: Int) {
        dao.delete(productId)
    }

    override suspend fun setFavorite(productId: Int, favorite: Boolean) {
        dao.setFavorite(productId, if (favorite) 1 else 0)
    }
}
