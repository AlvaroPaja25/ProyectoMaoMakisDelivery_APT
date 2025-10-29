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
    version = 5,
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
                    // Upsert de categorías base en cada apertura para garantizar nuevas categorías y/o íconos
                    upsertBaseCategories(database)
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
            categoryDao.insertAll(baseCategories())

            // 3. Productos de ejemplo (mínimo 3 por categoría)
            val products = listOf(
                // Categoría 1: Makis Clásicos
                Product(id = 1, categoryId = 1, score = "9", name = "Maki Acevichado", price = 15.50, description = "Relleno de langostino, cubierto con atún y salsa acevichada.", tipoPlato = 1, iconResName = "maki_acevichado"),
                Product(id = 2, categoryId = 1, score = "8", name = "California Roll", price = 12.00, description = "Clásico con palta, pepino y kanikama.", tipoPlato = 1, iconResName = "california_roll"),
                Product(id = 3, categoryId = 1, score = "7", name = "Philadelphia Roll", price = 14.00, description = "Queso crema, salmón y palta.", tipoPlato = 1, iconResName = "philadelphia_roll"),

                // Categoría 2: Makis Especiales
                Product(id = 4, categoryId = 2, score = "8", name = "Volcano Roll", price = 18.00, description = "Maki empanizado con topping de mariscos flambeados.", tipoPlato = 1, iconResName = "volcano_roll"),
                Product(id = 5, categoryId = 2, score = "9", name = "Dragon Roll", price = 19.50, description = "Aguacate, anguila, pepino y salsa especial.", tipoPlato = 1, iconResName = "dragon_roll"),
                Product(id = 6, categoryId = 2, score = "8", name = "Tiger Roll", price = 19.00, description = "Langostino furai, palta y topping picante.", tipoPlato = 1, iconResName = "tiger_roll"),

                // Categoría 3: Bebidas
                Product(id = 7, categoryId = 3, score = "6", name = "Inca Kola 500ml", price = 5.00, description = "La bebida de sabor nacional.", tipoPlato = 1, iconResName = "inca_kola_500ml"),
                Product(id = 8, categoryId = 3, score = "7", name = "Agua Cielo 600ml", price = 3.50, description = "Agua sin gas.", tipoPlato = 1, iconResName = "agua_cielo_600ml"),
                Product(id = 9, categoryId = 3, score = "7", name = "Té Verde", price = 4.50, description = "Refrescante y ligero.", tipoPlato = 1, iconResName = "te_verde"),

                // Categoría 4: Entradas
                Product(id = 10, categoryId = 4, score = "8", name = "Gyoza de Cerdo (5u)", price = 10.00, description = "Empanaditas japonesas al vapor.", tipoPlato = 1, iconResName = "gyoza_de_cerdo_5u"),
                Product(id = 11, categoryId = 4, score = "7", name = "Edamame", price = 8.00, description = "Vainas de soya al vapor con sal.", tipoPlato = 1, iconResName = "edamame"),
                Product(id = 12, categoryId = 4, score = "8", name = "Yakitori", price = 12.00, description = "Brochetas de pollo a la parrilla.", tipoPlato = 1, iconResName = "yakitori"),

                // Categoría 5: Postres
                Product(id = 13, categoryId = 5, score = "8", name = "Mochi", price = 9.00, description = "Pastelitos de arroz rellenos.", tipoPlato = 1, iconResName = "mochi"),
                Product(id = 14, categoryId = 5, score = "7", name = "Dorayaki", price = 9.50, description = "Panqueques rellenos de anko.", tipoPlato = 1, iconResName = "dorayaki"),
                Product(id = 15, categoryId = 5, score = "8", name = "Tempura Helado", price = 11.00, description = "Bola de helado en tempura.", tipoPlato = 1, iconResName = "tempura_helado"),

                // Categoría 6: Ramen
                Product(id = 16, categoryId = 6, score = "9", name = "Shoyu Ramen", price = 24.00, description = "Caldo de soya con fideos y cerdo.", tipoPlato = 1, iconResName = "shoyu_ramen"),
                Product(id = 17, categoryId = 6, score = "9", name = "Tonkotsu Ramen", price = 26.00, description = "Caldo cremoso de hueso de cerdo.", tipoPlato = 1, iconResName = "tonkotsu_ramen"),
                Product(id = 18, categoryId = 6, score = "8", name = "Miso Ramen", price = 25.00, description = "Caldo de miso con vegetales.", tipoPlato = 1, iconResName = "miso_ramen"),

                // Categoría 7: Sashimi
                Product(id = 19, categoryId = 7, score = "9", name = "Sashimi de Salmón", price = 22.00, description = "Cortes de salmón fresco.", tipoPlato = 1, iconResName = "sashimi_de_salmon"),
                Product(id = 20, categoryId = 7, score = "9", name = "Sashimi de Atún", price = 23.00, description = "Cortes de atún fresco.", tipoPlato = 1, iconResName = "sashimi_de_atun"),
                Product(id = 21, categoryId = 7, score = "8", name = "Sashimi Mixto", price = 24.00, description = "Selección de pescados del día.", tipoPlato = 1, iconResName = "sashimi_mixto")
            )
            productDao.insertAll(products)
        }

        private fun baseCategories(): List<Category> = listOf(
            Category(id = 1, name = "Makis Clásicos", description = "Los favoritos de siempre", iconResName = "makis_clasicos"),
            Category(id = 2, name = "Makis Especiales", description = "Combinaciones únicas", iconResName = "makis_especiales"),
            Category(id = 3, name = "Bebidas", description = "Para acompañar tu pedido", iconResName = "bebidas"),
            Category(id = 4, name = "Entradas", description = "Para abrir el apetito", iconResName = "entradas"),
            Category(id = 5, name = "Postres", description = "El toque dulce final", iconResName = "postres"),
            Category(id = 6, name = "Ramen", description = "Sopas tradicionales japonesas", iconResName = "ramen"),
            Category(id = 7, name = "Sashimi", description = "Cortes de pescado fresco", iconResName = "sashimi")
        )

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

        private suspend fun upsertBaseCategories(database: AppDatabase) {
            val categoryDao = database.categoryDao()
            categoryDao.insertAll(baseCategories())
        }
    }
}
