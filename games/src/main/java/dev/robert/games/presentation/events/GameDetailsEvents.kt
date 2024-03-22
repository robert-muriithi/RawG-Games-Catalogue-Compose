package dev.robert.games.presentation.events

sealed class GameDetailsEvents {
    data class BookmarkGame(
        val id: Int,
        val bookmarked: Boolean
    ) : GameDetailsEvents()
    data class UnBookmarkGame(
        val id: Int,
        val bookmarked: Boolean
    ) : GameDetailsEvents()

    data class NavigateToHome(
        val url: String
    ) : GameDetailsEvents()

    data class ShareGame(
        val game: String
    ) : GameDetailsEvents()

    data class RefreshEvent(val id: Int) : GameDetailsEvents()

    data class ErrorEvent(val message: String) : GameDetailsEvents()

    data class RetryEvent(val id: Int) : GameDetailsEvents()
}