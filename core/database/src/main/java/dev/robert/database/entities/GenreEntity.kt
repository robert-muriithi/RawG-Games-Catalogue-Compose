package dev.robert.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.robert.games.data.dto.category.GameResponse

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: Int,
    val gameResponses: List<GameResponse>,
    val gamesCount: Int,
    val imageBackground: String,
    val name: String,
    val slug: String
)
