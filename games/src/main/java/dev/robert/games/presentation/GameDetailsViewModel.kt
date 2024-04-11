package dev.robert.games.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.games.domain.model.game_details.GameDetailsModel
import dev.robert.games.domain.usecase.BookMarkGameUseCase
import dev.robert.games.domain.usecase.GetGameDetailsUseCase
import dev.robert.games.domain.usecase.GetLocalGameUseCase
import dev.robert.games.presentation.events.GameDetailsEvents
import dev.robert.network.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val getGameDetailsUseCase: GetGameDetailsUseCase,
    private val getLocalGameUseCase: GetLocalGameUseCase,
    private val bookmarkGameUseCase: BookMarkGameUseCase,
) : ViewModel() {

    private val _gameDeatilsState = mutableStateOf(UIState<GameDetailsModel>())
    val gameDetailsState = _gameDeatilsState as State<UIState<GameDetailsModel>>

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _gameDeatilsState.value =
            gameDetailsState.value.copy(isLoading = false, error = throwable.message)
        _eventsFlow.tryEmit(
            GameDetailsEvents.ErrorEvent(
                throwable.message ?: "An unexpected error occurred"
            )
        )
    }

    private val _eventsFlow = MutableSharedFlow<GameDetailsEvents>()
    val eventsFlow = _eventsFlow
    private lateinit var navController: NavController

    fun getLocalGame(id: Int) = getLocalGameUseCase(id)

    fun setNavController(navController: NavController) {
        this.navController = navController
    }


    fun getGameDetails(id: Int) {
        _gameDeatilsState.value = UIState(isLoading = true)
        viewModelScope.launch(exceptionHandler) {
            getGameDetailsUseCase(id).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        _gameDeatilsState.value =
                            gameDetailsState.value.copy(isLoading = false, data = it.value)
                    }

                    is Resource.Failure -> {
                        _gameDeatilsState.value = gameDetailsState.value.copy(
                            isLoading = false,
                            error = it.throwable.message ?: "An unexpected error occurred"
                        )
                        _eventsFlow.tryEmit(
                            GameDetailsEvents.ErrorEvent(
                                it.throwable.message ?: "An unexpected error occurred"
                            )
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: GameDetailsEvents) {
        when (event) {
            is GameDetailsEvents.BookmarkGame -> bookmarkGame(event.id, event.bookmarked)
            is GameDetailsEvents.UnBookmarkGame -> unBookmarkGame(event.id, event.bookmarked)
            is GameDetailsEvents.NavigateToHome -> navigateToHome(event.url)
            is GameDetailsEvents.ShareGame -> shareGame(event.game)
            is GameDetailsEvents.ErrorEvent -> _eventsFlow.tryEmit(event)
            is GameDetailsEvents.RetryEvent -> getGameDetails(event.id)
            is GameDetailsEvents.RefreshEvent -> getGameDetails(event.id)
            is GameDetailsEvents.NavigateBack -> navController.navigateUp()
        }
    }

    private fun bookmarkGame(id: Int, bookmarked: Boolean) {
        viewModelScope.launch {
            bookmarkGameUseCase(id, bookmarked)
            _eventsFlow.tryEmit(GameDetailsEvents.BookmarkGame(id, bookmarked))
        }
    }

    private fun unBookmarkGame(int: Int, bookmarked: Boolean) {
        _eventsFlow.tryEmit(GameDetailsEvents.UnBookmarkGame(int, bookmarked))
    }

    private fun navigateToHome(url: String) {
        _eventsFlow.tryEmit(GameDetailsEvents.NavigateToHome(url))
    }

    private fun shareGame(game: String) {
        _eventsFlow.tryEmit(GameDetailsEvents.ShareGame(game))
    }
}

data class UIState<T>(
    val error: String? = null,
    val isLoading: Boolean = false,
    val data: T? = null,
)