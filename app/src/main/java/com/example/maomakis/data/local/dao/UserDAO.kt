package com.example.maomakis.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maomakis.data.local.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user WHERE id = :userId")
    suspend fun delete(userId: Int)

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    fun getUserById(userId: Int): Flow<User?>
    
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user ORDER BY lastLogin DESC")
    fun getAllUsers(): Flow<List<User>>
}
