package dev.robert.favorites.presentation

import dev.robert.favorites.domain.model.Game

data class BookmarkScreenState(
    val isLoading: Boolean = false,
    val bookmarks: List<Game> = emptyList(),
    val error: String? = null
)