package dev.robert.favorites.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.favorites.domain.model.Game
import dev.robert.favorites.domain.usecase.ClearBookmarksUseCase
import dev.robert.favorites.domain.usecase.DeleteBookmarkUseCase
import dev.robert.favorites.domain.usecase.GetBookmarksUseCase
import dev.robert.network.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val clearBookMarksUseCase : ClearBookmarksUseCase,
    private val deleteBookMarkUseCase : DeleteBookmarkUseCase,
) : ViewModel() {

    private lateinit var navController: NavController


    private val _handler : CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }

    private val _uiState = mutableStateOf(BookmarkScreenState())
    val uiState = _uiState as State<BookmarkScreenState>


    fun setNavController(navController: NavController) {
        this.navController = navController
    }
   private fun getBookmarks() {
        _uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            getBookmarksUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Failure -> {
                        _uiState.value = uiState.value.copy(isLoading = false, error = it.throwable.message)
                    }
                    is Resource.Success -> {
                        _uiState.value = uiState.value.copy(isLoading = false, bookmarks = it.value)
                    }
                }
            }
        }
    }





    fun onEvent(event: BookMarkEvents) {
        when (event) {
            is BookMarkEvents.RemoveBookmark -> removeBookmark(event.id, event.isBookmark)
            is BookMarkEvents.ClearBookmarks -> clearBookmarks()
            is BookMarkEvents.NavigateToDetailScreen -> navigateToDetailScreen(event.game)
        }
    }

    private fun navigateToDetailScreen(game: Game) {
        navController.navigate("game_detail/${game.id}")
    }

    private fun clearBookmarks() {
        viewModelScope.launch(_handler){
            clearBookMarksUseCase.invoke()
        }
    }

    private fun removeBookmark(id: Int, isBookmark: Boolean) {
        viewModelScope.launch(_handler) {
            deleteBookMarkUseCase.invoke(id, isBookmark)
        }
    }

    init {
        getBookmarks()
    }

}