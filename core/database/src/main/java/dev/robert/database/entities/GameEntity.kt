package dev.robert.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    val category: String,
    val description: String,
    @PrimaryKey val id: Int,
    val image: String,
    val price: Double,
    val title: String
)


