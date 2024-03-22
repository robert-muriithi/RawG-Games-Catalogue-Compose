package dev.robert.games.presentation.events

sealed class HomeScreenEvent {
    data class NavigateToGameDetails(val id: Int) : HomeScreenEvent()
    object NavigateToSearch : HomeScreenEvent()

    object NavigateToBookmarks : HomeScreenEvent()

    data class NavigateToGenreDetails(val genre: String) : HomeScreenEvent()

    data class NavigateToCategory(val category: String) : HomeScreenEvent()
}