package dev.robert.games.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dev.robert.designsystem.presentation.CustomSnackBar
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.game_details.GameDetailsModel
import dev.robert.games.presentation.events.GameDetailsEvents
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GameDetailsScreen(
    viewModel: GameDetailsViewModel = hiltViewModel(),
    game: GamesResultModel,
) {
    val gameState = viewModel.gameDetailsState.value
    val eventsFlow = viewModel.eventsFlow
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        game.id?.let { viewModel.getGameDetails(it) }
        eventsFlow.collectLatest { event ->
            when (event) {
                is GameDetailsEvents.ErrorEvent -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GameDetailsComponent(game = game, event = viewModel::onEvent, gameState = gameState)
    }
}

@Composable
fun GameDetailsComponent(
    gameState: UIState<GameDetailsModel>,
    game: GamesResultModel,
    event: (GameDetailsEvents) -> Unit,
) {
    Scaffold(content = { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ErrorComponent
            ErrorOccurredComponent(gameState = gameState, event = event)

            // Loading
            LoadingComponent(gameState = gameState)

            // Success
            SuccessComponent(gameState = gameState, game = game)

            // Empty
//            EmptyComponent(gameState = gameState)
        }
    })
}


@Composable
fun LoadingComponent(
    gameState: UIState<GameDetailsModel>,
) {
    when {
        gameState.isLoading && gameState.error.isNullOrEmpty() -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

}

@Composable
fun BoxScope.ErrorOccurredComponent(
    gameState: UIState<GameDetailsModel>,
    event: (GameDetailsEvents) -> Unit,
) {
    val snackState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    if (gameState.error != null && gameState.isLoading.not()) {
        SnackbarHost(snackState, Modifier.align(Alignment.TopCenter)) {
            CustomSnackBar(
                message = gameState.error,
                action = {
                    event(GameDetailsEvents.RetryEvent(gameState.data?.id ?: 0))
                }
            )
        }
    }
}

@Composable
fun SuccessComponent(
    gameState: UIState<GameDetailsModel>,
    game: GamesResultModel,
) {
    if (gameState.data != null && gameState.isLoading.not() && gameState.error.isNullOrEmpty()) {
        GameDetailsContent(game = game, gameDetails = gameState.data)
    }
}

@Composable
fun GameDetailsContent(
    game: GamesResultModel,
    gameDetails: GameDetailsModel
) {

}



