package dev.robert.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.robert.database.entities.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(gamesRemoteKey: RemoteKey)

    @Query("DELETE FROM remote_keys WHERE id = :key")
    suspend fun delete(key: String)

    /*@Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeysGameId(id: Int): RemoteKey?*/


    @Query("select * from remote_keys where id=:key")
    suspend fun getKeyByGame(key: String): RemoteKey?

    @Query("SELECT * FROM remote_keys WHERE id = :key")
    suspend fun getKeyByGameId(key: Int): RemoteKey?


    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM remote_keys")
    suspend fun getAll(): List<RemoteKey>

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllKeys()
}