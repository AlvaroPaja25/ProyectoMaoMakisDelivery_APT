package com.example.maomakis.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.maomakis.data.local.dao.*
import com.example.maomakis.data.local.entity.*
import com.example.maomakis.data.security.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Category::class, Product::class, Carrito::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun categoryDao(): CategoryDAO
    abstract fun productDao(): ProductDAO
    abstract fun carritoDao(): CarritoDAO
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "maomakis_app.db"
                ).fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                    .also { INSTANCE = it }
            }
    }

    private class AppDatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateDatabase(it)
                }
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // Aseguramos que existan datos mínimos si por alguna razón la BD quedó vacía
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    ensureSeeded(database)
                }
            }
        }

        private suspend fun prepopulateDatabase(database: AppDatabase) {
            val userDao = database.userDao()
            val categoryDao = database.categoryDao()
            val productDao = database.productDao()

            // 1. Usuario de prueba
            val testUser = User(
                name = "Usuario de Prueba",
                email = "test@test.com",
                password = PasswordHasher.hashPassword("12345")
            )
            userDao.insert(testUser)

            // 2. Categorías de ejemplo
            val categories = listOf(
                Category(id = 1, name = "Makis Clásicos", description = "Los favoritos de siempre", iconResName = "pizza"),
                Category(id = 2, name = "Makis Especiales", description = "Combinaciones únicas", iconResName = "man"),
                Category(id = 3, name = "Bebidas", description = "Para acompañar tu pedido", iconResName = "man"),
                Category(id = 4, name = "Entradas", description = "Para abrir el apetito", iconResName = "pizza"),
                Category(id = 5, name = "Postres", description = "El toque dulce final", iconResName = "man")
            )
            categoryDao.insertAll(categories)

            // 3. Productos de ejemplo
            val products = listOf(
                Product(id = 1,categoryId = 1,rating = 5.0, name = "Maki Acevichado", price = 15.50, description = "Relleno de langostino, cubierto con atún y salsa acevichada.", tipoPlato = 1, iconResName = "fav1"),
                Product(id = 2,categoryId = 1,rating = 2.8, name = "California Roll", price = 12.00, description = "El clásico con palta, pepino y kanikama.", tipoPlato = 1, iconResName = "fav2"),
                Product(id = 3,categoryId = 2,rating = 3.7, name = "Volcano Roll", price = 18.00, description = "Maki empanizado con topping de mariscos flambeados.", tipoPlato = 1, iconResName = "fav3"),
                Product(id = 4,categoryId = 3,rating = 7.6, name = "Inca Kola 500ml", price = 5.00, description = "La bebida de sabor nacional.", tipoPlato = 1, iconResName = "man"),
                Product(id = 5,categoryId = 4,rating = 1.2, name = "Gyoza de Cerdo (5u)", price = 10.00, description = "Empanaditas japonesas al vapor.", tipoPlato = 1, iconResName = "lunch")
            )
            productDao.insertAll(products)
        }

        private suspend fun ensureSeeded(database: AppDatabase) {
            val categoryDao = database.categoryDao()
            val productDao = database.productDao()

            // Si faltan categorías o productos, reinsertamos los datos base
            val categoriesCount = categoryDao.count()
            val productsCount = productDao.count()

            if (categoriesCount == 0 || productsCount == 0) {
                prepopulateDatabase(database)
            }
        }
    }
}
