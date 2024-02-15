package dev.robert.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.robert.database.converters.ProductEntityConverters
import dev.robert.database.dao.CategoriesDao
import dev.robert.database.dao.GamesDao
import dev.robert.database.entities.CategoryEntity
import dev.robert.database.entities.GameEntity

@Database(entities = [GameEntity::class, CategoryEntity::class], version = 1, exportSchema = false)
@TypeConverters(ProductEntityConverters::class)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
    abstract fun categoriesDao(): CategoriesDao
}