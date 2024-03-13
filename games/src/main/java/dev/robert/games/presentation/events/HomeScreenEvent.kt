package dev.robert.games.presentation.events

sealed class HomeScreenEvent {
    object NavigateToGameDetails : HomeScreenEvent()
    object NavigateToSearch : HomeScreenEvent()

    data class NavigateToCategory(val category: String) : HomeScreenEvent()
}