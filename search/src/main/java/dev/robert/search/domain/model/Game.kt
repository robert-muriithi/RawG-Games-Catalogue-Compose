package dev.robert.search.domain.model

import dev.robert.network.dto.dto.games.EsrbRating
import dev.robert.network.dto.dto.games.GenreResponseDto
import dev.robert.network.dto.dto.games.ParentPlatformDto
import dev.robert.network.dto.dto.games.Rating
import dev.robert.network.dto.dto.games.ShortScreenshot
import dev.robert.network.dto.dto.games.Tag

data class Game(
    val added: Int? = null,
    val backgroundImage: String? = null,
    val clip: String? = null,
    val dominantColor: String? = null,
    val esrbRating: EsrbRating? = null,
    val genreResponseDtos: List<GenreResponseDto>? = null,
    val id: Int,
    val metacritic: Int? = null,
    val name: String? = null,
    val parentPlatformDtos: List<ParentPlatformDto>? = null,
    val playtime: Int? = null,
    val rating: Double? = null,
    val ratingTop: Int? = null,
    val ratings: List<Rating>? = null,
    val ratingsCount: Int? = null,
    val released: String? = null,
    val reviewsCount: Int? = null,
    val reviewsTextCount: Int? = null,
    val saturatedColor: String? = null,
    val shortScreenshots: List<ShortScreenshot>? = null,
    val suggestionsCount: Int? = null,
    val tags: List<Tag>? = null,
    val isBookMarked: Boolean = false,
    val searchQuery: String? = null,
    val recentSearch : Boolean = false
)
