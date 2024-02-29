package dev.robert.games.domain.model.genre


data class Genre(
    val gameResponses: List<Game>,
    val gamesCount: Int,
    val id: Int,
    val imageBackground: String,
    val name: String,
    val slug: String
)
