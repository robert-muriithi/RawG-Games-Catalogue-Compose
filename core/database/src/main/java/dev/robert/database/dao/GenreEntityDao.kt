package dev.robert.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.robert.database.entities.GenreEntity

@Dao
interface GenreEntityDao {
    @Insert
    suspend fun insertCategory(category: GenreEntity)

    @Insert
    suspend fun insertAll(categories: List<GenreEntity>)

    @Delete
    suspend fun deleteCategory(category: GenreEntity)

    @Query(value = "DELETE FROM genres")
    suspend fun deleteAll()


    @Query(value = "SELECT * FROM genres")
    fun getAllGenres(): PagingSource<Int, GenreEntity>
}