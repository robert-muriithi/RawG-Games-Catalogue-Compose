package dev.robert.favorites.presentation

import dev.robert.favorites.domain.model.Game

sealed class BookMarkEvents {
    data class NavigateToDetailScreen(val game: Game): BookMarkEvents()
    data object ClearBookmarks: BookMarkEvents()
    data class RemoveBookmark(val id: Int, val isBookmark: Boolean): BookMarkEvents()

}