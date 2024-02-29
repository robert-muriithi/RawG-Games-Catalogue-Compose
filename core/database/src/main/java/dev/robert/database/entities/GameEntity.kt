package dev.robert.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.robert.network.dto.dto.games.EsrbRating
import dev.robert.network.dto.dto.games.GenreResponseDto
import dev.robert.network.dto.dto.games.ParentPlatformDto
import dev.robert.network.dto.dto.games.Rating
import dev.robert.network.dto.dto.games.ShortScreenshot
import dev.robert.network.dto.dto.games.Tag

@Entity(tableName = "games")
data class GameEntity(
    val added: Int,
    val backgroundImage: String,
    val clip: Any,
    val dominantColor: String,
    val esrbRating: EsrbRating,
    val genreResponseDtos: List<GenreResponseDto>,
    @PrimaryKey val id: Int,
    val metacritic: Int,
    val name: String,
    val parentPlatformDtos: List<ParentPlatformDto>,
    val playtime: Int,
    val rating: Double,
    val ratingTop: Int,
    val ratings: List<Rating>,
    val ratingsCount: Int,
    val released: String,
    val reviewsCount: Int,
    val reviewsTextCount: Int,
    val saturatedColor: String,
    val shortScreenshots: List<ShortScreenshot>,
    val suggestionsCount: Int,
    val tags: List<Tag>,
)


