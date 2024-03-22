package dev.robert.games.presentation.genres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.usecase.GetGamesUseCase
import dev.robert.games.domain.usecase.GetGenreGamesUseCase
import dev.robert.games.presentation.StateHolder
import dev.robert.games.presentation.events.GenreGamesEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreDetailsViewModel @Inject constructor(
    private val getGamesUseCase: GetGenreGamesUseCase,
) : ViewModel() {

    private lateinit var navController: NavController

    private val _gamesState = mutableStateOf(StateHolder<Flow<PagingData<GamesResultModel>>>())
    val gamesState: State<StateHolder<Flow<PagingData<GamesResultModel>>>> = _gamesState

    fun getGames(genres: String) {
        _gamesState.value = gamesState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val games = getGamesUseCase.invoke(genre = genres).cachedIn(viewModelScope)
            _gamesState.value = gamesState.value.copy(
                isLoading = false,
                data = games
            )
        }
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }


    fun onEvent(event: GenreGamesEvents) {
        when(event) {
            is GenreGamesEvents.NavigateToGameDetails -> {
                // navController.navigate(Destinations.GameDetailsScreen.route + "/${event.id}")
            }
        }
    }



}