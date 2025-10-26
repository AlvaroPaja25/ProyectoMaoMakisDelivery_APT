package com.example.maomakis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maomakis.domain.model.ProductListModel
import com.example.maomakis.domain.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProductViewModel(productRepository: ProductRepository) : ViewModel() {

    /**
     * Flujo que emite la lista completa de productos desde el repositorio.
     * Se mantiene activo mientras la UI esté suscrita (con un timeout de 5s).
     */
    val products: StateFlow<List<ProductListModel>> = productRepository.getAllProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
