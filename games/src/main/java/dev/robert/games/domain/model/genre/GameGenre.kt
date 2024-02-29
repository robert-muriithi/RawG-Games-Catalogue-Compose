package dev.robert.games.domain.model.genre



data class GameGenre(
    val count: Int,
    val next: String?,
    val previous: String?,
    val genreResponses: List<Genre>
)
