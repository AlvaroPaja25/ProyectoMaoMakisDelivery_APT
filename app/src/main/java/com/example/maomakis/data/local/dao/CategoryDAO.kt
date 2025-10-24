package com.example.maomakis.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.maomakis.data.local.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Update
    suspend fun update(category: Category)

    @Query("DELETE FROM category WHERE id = :categoryId")
    suspend fun delete(categoryId: Int)

    @Query("SELECT * FROM category ORDER BY name ASC")
    fun getAll(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<Category?>

    @Query("SELECT COUNT(*) FROM category")
    suspend fun count(): Int
}
