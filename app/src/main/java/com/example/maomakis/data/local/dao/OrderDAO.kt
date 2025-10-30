package com.example.maomakis.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.maomakis.data.local.entity.Order
import com.example.maomakis.data.local.entity.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<OrderItem>)

    @Transaction
    suspend fun insertOrderWithItems(order: Order, items: List<OrderItem>): Long {
        val orderId = insertOrder(order)
        val withIds = items.map { it.copy(orderId = orderId) }
        insertItems(withIds)
        return orderId
    }

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun countOrders(): Int

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getItemsForOrder(orderId: Long): List<OrderItem>
}
