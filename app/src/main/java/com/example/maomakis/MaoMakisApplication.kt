package com.example.maomakis

import android.app.Application
import com.example.maomakis.data.local.AppDatabase
import com.example.maomakis.data.local.SessionManager
import com.example.maomakis.data.repository.AuthRepositoryImpl
import com.example.maomakis.data.repository.CarritoRepositoryImpl
import com.example.maomakis.data.repository.CategoryRepositoryImpl
import com.example.maomakis.data.repository.ProductRepositoryImpl
import com.example.maomakis.data.repository.UserRepositoryImpl
import com.example.maomakis.domain.repository.AuthRepository
import com.example.maomakis.domain.repository.CarritoRepository
import com.example.maomakis.domain.repository.CategoryRepository
import com.example.maomakis.domain.repository.ProductRepository
import com.example.maomakis.domain.repository.UserRepository

class MaoMakisApplication : Application() {

    private val db by lazy { AppDatabase.getInstance(this) }

    val authRepository: AuthRepository by lazy {
        val sessionManager = SessionManager(this)
        AuthRepositoryImpl(db.userDao(), sessionManager)
    }

    val userRepository: UserRepository by lazy {
        val sessionManager = SessionManager(this)
        UserRepositoryImpl(db.userDao(), sessionManager)
    }

    val carritoRepository: CarritoRepository by lazy {
        CarritoRepositoryImpl(db.carritoDao(), this)
    }

    // Añadimos el repositorio de productos
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(db.productDao(), this)
    }

    // Repositorio de categorías
    val categoryRepository: CategoryRepository by lazy {
        CategoryRepositoryImpl(db.categoryDao(), this)
    }
}
