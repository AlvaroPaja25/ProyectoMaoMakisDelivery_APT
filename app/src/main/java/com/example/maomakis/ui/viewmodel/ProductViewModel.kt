package com.example.maomakis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maomakis.domain.model.ProductListModel
import com.example.maomakis.domain.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    val products: StateFlow<List<ProductListModel>> = productRepository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteProducts: StateFlow<List<ProductListModel>> = productRepository.getFavoriteProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getProductsByCategory(categoryId: Int): Flow<List<ProductListModel>> =
        productRepository.getProductsByCategory(categoryId)

    fun getProductsTop10ByRating(): Flow<List<ProductListModel>> =
        productRepository.getProductsTop10ByRating()

    suspend fun getProductsLast10Static(): List<ProductListModel> {
        return productRepository.getProductsLast10Added()
    }

    suspend fun getProductsByTipoPlato(tipoPlato: Int): List<ProductListModel> {
        return productRepository.getProductsByTipoPlato(tipoPlato)
    }

    fun toggleFavorite(productId: Int, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            try {
                productRepository.setFavorite(productId, !currentIsFavorite)
            } catch (t: Throwable) {
                // Manejo simple, extiende según tu estrategia (logs, eventos UI, etc.)
            }
        }
    }

    fun observeProductById(productId: Int): StateFlow<ProductListModel?> =
        productRepository.getProductById(productId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), null)

    fun searchProducts(query: String): Flow<List<ProductListModel>> =
        productRepository.searchProducts(query)
}
