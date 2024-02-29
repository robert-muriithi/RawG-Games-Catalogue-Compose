package dev.robert.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.robert.database.converters.GameEntityConverters
import dev.robert.database.dao.GenreEntityDao
import dev.robert.database.dao.GameDao
import dev.robert.database.entities.GenreEntity
import dev.robert.database.entities.GameEntity

@Database(entities = [GameEntity::class, GenreEntity::class], version = 1, exportSchema = false)
@TypeConverters(GameEntityConverters::class)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun gameEntityDao(): GameDao
    abstract fun genreEntityDao(): GenreEntityDao
}