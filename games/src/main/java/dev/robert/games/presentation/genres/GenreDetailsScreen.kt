package dev.robert.games.presentation.genres

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.robert.designsystem.presentation.CustomSnackBar
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.presentation.StateHolder
import dev.robert.games.presentation.components.HorizontalGameItem
import dev.robert.games.presentation.events.GenreGamesEvents
import kotlinx.coroutines.flow.Flow
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreDetailsScreen(
    viewModel: GenreDetailsViewModel = hiltViewModel(),
    genres: String,
    navController: NavController
) {
    val games = viewModel.gamesState.value.data?.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.setNavController(navController)
        viewModel.getGames(genres = genres.lowercase(Locale.ROOT))
    })
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(key1 = pullToRefreshState, block = {
        when (pullToRefreshState.isRefreshing) {
            true -> {
                viewModel.getGames(genres = genres.lowercase(Locale.ROOT))
                pullToRefreshState.endRefresh()
            }
            else -> Unit
        }
    })

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                Text(
                    text = "$genres Games",
                    style = MaterialTheme.typography.titleLarge,
                )
            },
                navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
                scrollBehavior = scrollBehavior,
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                // Loading
                GameLoadingComponent(state = viewModel.gamesState)
                // Error
                ErrorComponent(
                    state = viewModel.gamesState,
                    retry = {
                        viewModel.getGames(genres = genres.lowercase(Locale.ROOT))
                    }
                )
                // Content
                GenreDetailsContent(
                    games = games,
                    state = viewModel.gamesState,
                    onClick = { id ->
                        viewModel.onEvent(
                            GenreGamesEvents.NavigateToGameDetails(id = id)
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun BoxScope.GameLoadingComponent(
    state: State<StateHolder<Flow<PagingData<GamesResultModel>>>>,
    modifier: Modifier = Modifier,
) {
    if (state.value.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .testTag("GameLoadingComponent")
        )
    }
}

@Composable
fun ErrorComponent(
    state: State<StateHolder<Flow<PagingData<GamesResultModel>>>>,
    retry: () -> Unit,
) {
    if (!state.value.isLoading && state.value.error != null) {
        CustomSnackBar(
            message = state.value.error ?: "An error occurred",
        ) {
            retry()
        }
    }
}



@Composable
fun BoxScope.GenreDetailsContent(
    games: LazyPagingItems<GamesResultModel>?,
    state: State<StateHolder<Flow<PagingData<GamesResultModel>>>>,
    onClick: (Int) -> Unit,
) {
    if (!state.value.isLoading && state.value.error == null && state.value.data != null) {
        GenreDetailsList(gamesState = games!!, onClick = onClick)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.GenreDetailsList(
    gamesState: LazyPagingItems<GamesResultModel>,
    onClick: (Int) -> Unit,
) {
    val state = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize()
    ) {
        if (!pullToRefreshState.isRefreshing){
            items(gamesState.itemCount) { index ->
                gamesState[index]?.let { game ->
                    HorizontalGameItem(game = game, onClick = onClick)
                }
            }
        }
    }
    PullToRefreshContainer(
        modifier = Modifier.align(Alignment.TopCenter),
        state = pullToRefreshState,
    )
}

