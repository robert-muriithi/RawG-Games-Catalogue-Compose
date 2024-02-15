package dev.robert.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.robert.database.entities.GameEntity
@Dao
interface GamesDao {
    @Query(value = "SELECT * FROM games")
    suspend fun getAllProducts(): List<GameEntity>

    @Query(value = "SELECT * FROM games WHERE id = :id")
    suspend fun getProductById(id: Int): GameEntity

    @Delete
    suspend fun deleteProduct(product: GameEntity)

    @Query(value = "DELETE FROM games")
    suspend fun deleteAllProducts()

    @Insert
    suspend fun insertProduct(product: GameEntity)

    @Insert
    suspend fun insertProducts(product: List<GameEntity>)
}