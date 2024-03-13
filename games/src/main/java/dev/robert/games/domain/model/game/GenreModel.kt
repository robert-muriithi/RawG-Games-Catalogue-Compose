package dev.robert.games.domain.model.game


data class GenreModel(
    val gamesCount: Int,
    val id: Int,
    val imageBackground: String? = null,
    val name: String,
    val slug: String
)