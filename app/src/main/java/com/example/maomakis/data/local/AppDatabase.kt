package com.example.maomakis.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.maomakis.data.local.entity.*
import com.example.maomakis.data.local.dao.*

@Database(
    entities = [User::class,Category::class,Product::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract  fun userDao(): UserDAO
    abstract  fun categoryDao(): CategoryDAO
    abstract  fun productDao(): ProductDAO
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "maomakis_app.db"
                )
//                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}