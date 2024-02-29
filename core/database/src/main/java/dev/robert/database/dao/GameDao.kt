package dev.robert.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.robert.database.entities.GameEntity
import dev.robert.database.entities.GenreEntity

@Dao
interface GameDao {
    @Query(value = "SELECT * FROM games")
    suspend fun getAllGames(): PagingSource<Int, GameEntity>

    @Query(value = "SELECT * FROM games WHERE id = :id")
    suspend fun getGameById(id: Int): GameEntity

    @Query(value = "SELECT * FROM games WHERE name LIKE :name")
    suspend fun getGameByName(name: String): List<GameEntity>

    @Delete
    suspend fun deleteProduct(product: GameEntity)

    @Query(value = "DELETE FROM games")
    suspend fun deleteAllGames()

    @Insert
    suspend fun insertGame(product: GameEntity)

    @Insert
    suspend fun insertGame(product: List<GameEntity>)
}