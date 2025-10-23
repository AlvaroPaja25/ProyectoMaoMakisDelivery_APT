package com.example.maomakis.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.maomakis.data.local.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(product: List<Product>)

    @Update
    suspend fun update(product: Product)

    @Query("DELETE FROM product WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM product ORDER BY name ASC")

    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE categoryId = :categoryId ORDER BY name ASC")
    fun getByCategory(categoryId: Int): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE favorite = 1 ORDER BY name ASC")
    fun getFavorites(): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<Product?>

    @Query("UPDATE product SET favorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Int, favorite: Int)

    @Query("SELECT COUNT(*) FROM product")
    suspend fun count(): Int
}