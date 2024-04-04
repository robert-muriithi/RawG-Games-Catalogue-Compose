package dev.robert.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/*
@Dao
interface GenresRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genresRemoteKeys: List<GenresRemoteKey>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genresRemoteKey: GenresRemoteKey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(gamesRemoteKey: GenresRemoteKey)

    @Query("DELETE FROM genre_remote_keys WHERE id = :key")
    suspend fun delete(key: String)



    @Query("select * from genre_remote_keys where id=:key")
    suspend fun getKeyByGenre(key: String): GenresRemoteKey?

    @Query("SELECT * FROM genre_remote_keys WHERE id = :key")
    suspend fun getKeyByGenreId(key: Int): GenresRemoteKey?


    @Query("DELETE FROM genre_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM genre_remote_keys")
    suspend fun getAll(): List<GenresRemoteKey>

    @Query("DELETE FROM genre_remote_keys")
    suspend fun deleteAllKeys()
}*/
