package dev.robert.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int? = null,
    val category: String
)
