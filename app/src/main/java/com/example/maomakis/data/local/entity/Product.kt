package com.example.maomakis.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"])
    ],
    indices = [Index("categoryId")]
)
data class Product(
   @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val favorite: Int = 0,
    val rating: Double = 0.0,
    val name: String,
    val price: Double,
    val description: String?,
    val tipoPlato: Int = 0,
    val iconResName: String?
)
