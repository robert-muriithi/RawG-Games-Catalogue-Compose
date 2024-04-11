package dev.robert.games.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.robert.designsystem.presentation.CustomSnackBar
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.game_details.GameDetailsModel
import dev.robert.designsystem.presentation.NetworkImage
import dev.robert.games.presentation.components.GenresRow
import dev.robert.games.presentation.components.RatingBar
import dev.robert.games.presentation.events.GameDetailsEvents
import dev.robert.games.utils.shareLink
import dev.robert.navigation.navigation.Destinations
import dev.robert.shared.utils.ConverterDate
import dev.robert.shared.utils.convertDateTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun GameDetailsScreen(
    viewModel: GameDetailsViewModel = hiltViewModel(),
    gameId: Int,
    navController: NavController
) {
    val gameState = viewModel.gameDetailsState.value
    val eventsFlow = viewModel.eventsFlow
    val context = LocalContext.current
    val game = viewModel.getLocalGame(gameId).collectAsStateWithLifecycle(initialValue = null).value

    LaunchedEffect(key1 = Unit) {
        viewModel.setNavController(navController)
        viewModel.getGameDetails(gameId)
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
        GameDetailsComponent(
            game = game,
            event = viewModel::onEvent,
            gameState = gameState,
            onBookmark = { id, bookmarked ->
                viewModel.onEvent(GameDetailsEvents.BookmarkGame(id, bookmarked))
            }
        )

    }
}

@Composable
fun GameDetailsComponent(
    gameState: UIState<GameDetailsModel>,
    game: GamesResultModel?,
    event: (GameDetailsEvents) -> Unit,
    onBookmark: (Int, Boolean) -> Unit,
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
            SuccessComponent(
                gameState = gameState,
                game = game,
                event = event,
                onBookmark = onBookmark,
            )

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
    game: GamesResultModel?,
    event: (GameDetailsEvents) -> Unit,
    onBookmark: (Int, Boolean) -> Unit,
) {
    if (gameState.data != null && gameState.isLoading.not() && gameState.error.isNullOrEmpty()) {
        GameDetailsContent(
            game = game,
            gameDetails = gameState.data,
            event = event,
            onBookMark = onBookmark,
        )
    }
}

@Composable
fun GameDetailsContent(
    game: GamesResultModel?,
    gameDetails: GameDetailsModel,
    event: (GameDetailsEvents) -> Unit,
    onBookMark: (Int, Boolean) -> Unit,
) {
    val scrollState = rememberScrollState()
    var savedState by remember {
        mutableStateOf(game?.isBookMarked ?: false)
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        game?.let { game ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy((-30).dp),
            ) {
                GameDetailsHeader(game = game, gameDetails = gameDetails, event = event)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(
                            MaterialTheme.colorScheme.surface
                        )
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = game.name ?: "",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1F)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Icon(
                            imageVector = if (savedState) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(top = 4.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false),
                                    onClick = {
                                        savedState = !savedState
                                        onBookMark(game.id!!, savedState)
                                        if (savedState) Toast
                                            .makeText(
                                                context,
                                                "Bookmarked",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(36.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Metascore",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                ) {
                                    Text(
                                        text = if (game.metacritic != 0) game.metacritic.toString() else "-",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "Rating",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                RatingBar(
                                    rating = game.rating ?: 0.0,
                                    maxStars = 5
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(40.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Released",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                game.released?.let {
                                    Text(
                                        text = it.convertDateTo(ConverterDate.FULL_DATE),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "Genre",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                GenresRow(
                                    game = game,
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .background(
                                            MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.5f),
                                            MaterialTheme.shapes.small
                                        )
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    gameDetails.description?.let {
                        Text(
                            text = it.ifBlank { "-" },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Justify
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            /*state.shareSheetGame?.let {
                viewModel.onEvent(DetailScreenEvent.ShareGame(dismissed = true))
            }*/
        }
    }
}

@Composable
fun ImageSlider(game: GamesResultModel?) {
    var currentImageIndex by remember { mutableIntStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val screenshots = game?.shortScreenshots ?: emptyList()

    LaunchedEffect(currentImageIndex) {
        while (true) {
            delay(3000L)
            if (!isAnimating) {
                val nextIndex = (currentImageIndex + 1) % screenshots.size
                currentImageIndex = nextIndex
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                itemsIndexed(screenshots) { index, image ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                if (index != currentImageIndex && !isAnimating) {
                                    isAnimating = true
                                    coroutineScope.launch {
                                        val delayMillis = 500L
                                        delay(delayMillis / 2)
                                        currentImageIndex = index
                                        delay(delayMillis)
                                        isAnimating = false
                                    }
                                }
                            }
                    ) {
                        NetworkImage(
                            contentDescription = "",
                            url = screenshots[index].image,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = game?.name ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1F)
                )
                Icon(
                    imageVector = if (game?.isBookMarked == true) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(top = 4.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                            onClick = {
                                game?.id
                                    ?.let {
                                        GameDetailsEvents.BookmarkGame(
                                            id = it,
                                            bookmarked = !game.isBookMarked
                                        )
                                    }
                                    ?.let {

                                    }
                            }

                        )
                )
            }
        }
    }
}

@Composable
fun GameDetailsHeader(
    game: GamesResultModel?,
    gameDetails: GameDetailsModel,
    event: (GameDetailsEvents) -> Unit,
) {
    val context = LocalContext.current
    var isBookMarked by remember { mutableStateOf(game?.isBookMarked ?: false) }
    val hasScreenshots by remember { mutableStateOf(game?.shortScreenshots?.isNotEmpty()) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        hasScreenshots.let {
            if (it == true) {
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                ) {
                    ImageSlider(game = game)
                }

            } else {
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                ) {
                    NetworkImage(
                        url = game?.backgroundImage ?: gameDetails.backgroundImage ?: "",
                        contentDescription = game?.name ?: "Game Image",
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false),
                        onClick = {
                            event(GameDetailsEvents.NavigateBack)
                        }
                    )
            )
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false),
                        onClick = {
                            context.shareLink("Find more about ${game?.name ?: ""}")
                            event(GameDetailsEvents.ShareGame(game?.name ?: ""))
                        }
                    )

            )

        }
    }
}

@Composable
fun BackgroundBox(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Transparent,
                    ),
                    center = Offset.Zero,
                    radius = 3000f
                )
            ),
    )
}






