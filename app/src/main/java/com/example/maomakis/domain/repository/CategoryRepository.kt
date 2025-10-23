package com.example.maomakis.domain.repository

import com.example.maomakis.domain.model.CategoryListModel
import com.example.maomakis.domain.model.CategoryRegisterModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<CategoryListModel>>
    suspend fun insert(category: CategoryRegisterModel)
    suspend fun update(category: CategoryRegisterModel)
    suspend fun delete(categoryId: Int)
    fun getCategoryById(categoryId: Int): Flow<CategoryListModel?>
}
