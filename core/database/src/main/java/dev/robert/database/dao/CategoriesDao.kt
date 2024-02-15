package dev.robert.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.robert.database.entities.CategoryEntity

@Dao
interface CategoriesDao {
    @Insert
    suspend fun insertCategory(category: CategoryEntity)

    @Insert
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query(value = "DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query(value = "SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>
}