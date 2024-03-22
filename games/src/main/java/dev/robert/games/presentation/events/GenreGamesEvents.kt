package dev.robert.games.presentation.events

sealed class GenreGamesEvents {
    data class NavigateToGameDetails(val id: Int) : GenreGamesEvents()
}