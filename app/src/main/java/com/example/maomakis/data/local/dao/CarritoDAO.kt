package com.example.maomakis.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maomakis.data.local.entity.Carrito
import kotlinx.coroutines.flow.Flow

data class CarritoWithProductData(
    // Campos de Product
    val productId: Int,
    val name: String,
    val price: Double,
    val iconResName: String?,
    val cant: Int,
    val score: String
)

@Dao
interface CarritoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(carrito: List<Carrito>)

    @Query("""
        SELECT 
            p.id as productId, 
            p.name, 
            p.price, 
            p.iconResName, 
            c.cant, 
            p.score
        FROM carrito c 
        INNER JOIN product p ON c.productId = p.id 
        WHERE c.userId = :userId
        ORDER BY c.productId ASC
    """)
    fun getCartItemsForUser(userId: Int): Flow<List<CarritoWithProductData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItem(item: Carrito)

    @Query("DELETE FROM carrito WHERE userId = :userId AND productId = :productId")
    suspend fun deleteItem(userId: Int, productId: Int)

    @Query("SELECT * FROM carrito WHERE userId = :userId AND productId = :productId LIMIT 1")
    suspend fun getItem(userId: Int, productId: Int): Carrito?
}
