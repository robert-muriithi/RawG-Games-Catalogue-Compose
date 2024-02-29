package dev.robert.games.domain.model.game


data class GamesResponseModel(
    val count: Int,
    val next: String,
    val previous: String?,
    val gamesResponseResults: List<GamesResultModel>,
)