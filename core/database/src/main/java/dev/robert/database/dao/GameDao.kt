package dev.robert.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.robert.database.entities.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query(value = "SELECT * FROM games")
    fun getAllGames(): PagingSource<Int, GameEntity>

    @Query(value = "SELECT * FROM games WHERE id = :id")
    suspend fun getGameById(id: Int): GameEntity

    @Query(value = "SELECT * FROM games WHERE name LIKE :name")
    fun getGameByName(name: String): PagingSource<Int, GameEntity>

    @Delete
    suspend fun deleteProduct(product: GameEntity)

    @Query(value = "DELETE FROM games")
    suspend fun deleteAllGames()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(product: GameEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(product: List<GameEntity>)

    @Query("UPDATE games SET isBookMarked = :bookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Int, bookmarked: Boolean)

    @Query("SELECT * FROM games WHERE rating > 4 order by rating desc limit :limit")
    fun getGamesAsFow(limit: Int): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE isBookMarked = 1")
    fun getBookmarkedGames(): PagingSource<Int, GameEntity>

    @Query("SELECT * FROM games WHERE genreResponseDtos LIKE '%'|| :genre ||'%'")
    fun getGamesByGenre(genre: String? = ""): PagingSource<Int, GameEntity>
}