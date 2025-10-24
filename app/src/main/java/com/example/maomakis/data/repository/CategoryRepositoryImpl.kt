package com.example.maomakis.data.repository

import android.content.Context
import com.example.maomakis.data.local.dao.CategoryDAO
import com.example.maomakis.data.mappers.toEntity
import com.example.maomakis.data.mappers.toListModel
import com.example.maomakis.domain.model.CategoryListModel
import com.example.maomakis.domain.model.CategoryRegisterModel
import com.example.maomakis.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val dao: CategoryDAO,
    private val context: Context
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<CategoryListModel>> =
        dao.getAll().map { list -> list.map { it.toListModel(context) } }

    override suspend fun insert(category: CategoryRegisterModel) {
        dao.insert(category.toEntity())
    }

    override suspend fun update(category: CategoryRegisterModel) {
        dao.update(category.toEntity())
    }

    override suspend fun delete(categoryId: Int) {
        dao.delete(categoryId)
    }

    override fun getCategoryById(categoryId: Int): Flow<CategoryListModel?> =
        dao.getById(categoryId).map { it?.toListModel(context) }
}
