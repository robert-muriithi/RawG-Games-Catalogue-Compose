package dev.robert.favorites.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.robert.designsystem.presentation.NetworkImage
import dev.robert.designsystem.presentation.cardColor
import dev.robert.favorites.domain.model.Game
import dev.robert.shared.utils.ConverterDate
import dev.robert.shared.utils.convertDateTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<BookmarksViewModel>()
    val uiState =  viewModel.uiState.value
    viewModel.setNavController(navController)

    Scaffold (
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(title = {
                Text("Bookmarks")
            })
        }
    ){ paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            // Loading
            BookmarksLoadingComponent(uiState = uiState)

            // Error
            BookMarksErrorStateComponent(uiState =uiState)

            // Data is Empty
            BookMarksEmptyStateComponent(uiState = uiState)

            // There is Data
            BookMarksSuccessStateComponent(
                uiState = uiState,
                onDeleteBookMark = { id, isBookMark ->
                    viewModel.onEvent(
                        BookMarkEvents.RemoveBookmark(id, isBookMark)
                    )
                },
                onClearBookMarks = {
                    viewModel.onEvent(BookMarkEvents.ClearBookmarks)
                },
                onNavigateToDetailScreen = { game ->
                    viewModel.onEvent(BookMarkEvents.NavigateToDetailScreen(game))
                }
            )
        }
    }
}

@Composable
fun BoxScope.BookMarksErrorStateComponent(
    uiState: BookmarkScreenState
) {
    if (!uiState.isLoading && uiState.error != null) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .testTag("BookMarksErrorStateComponent"),
            text = uiState.error
        )
    }
}

@Composable
fun BoxScope.BookMarksEmptyStateComponent(
    uiState: BookmarkScreenState
) {
    if (!uiState.isLoading && uiState.error == null && uiState.bookmarks.isEmpty()) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .testTag("BookMarksEmptyStateComponent"),
            text = "Nothing added to bookmarks yet."
        )
    }
}

@Composable
fun BoxScope.BookmarksLoadingComponent(
    uiState: BookmarkScreenState
) {
    if (uiState.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .testTag("BookmarksLoadingComponent")
        )
    }
}

@Composable
fun BookMarksSuccessStateComponent(
    uiState: BookmarkScreenState,
    onDeleteBookMark: (Int, Boolean) -> Unit,
    onClearBookMarks: () -> Unit,
    onNavigateToDetailScreen: (Game) -> Unit
) {
    if (!uiState.isLoading && uiState.error == null && uiState.bookmarks.isNotEmpty()) {
        val items = uiState.bookmarks
        LazyColumn(modifier = Modifier.testTag("BookMarksSuccessStateComponent")) {
            items(
                items = items,
                key = { it.id }
            ) { game->
                BookMarkItem(
                    game = game,
                    onNavigateToDetailScreen = onNavigateToDetailScreen,
                    onDeleteBookMark = { id, isBookMark ->
                        items.toMutableList().remove(game)
                        onDeleteBookMark(id, isBookMark)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMarkItem(
    game: Game,
    onNavigateToDetailScreen: (Game) -> Unit,
    onDeleteBookMark: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state->
            when (state) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDeleteBookMark(game.id, true)
                    return@rememberSwipeToDismissBoxState true
                }
                else -> {
                    return@rememberSwipeToDismissBoxState false
                }
            }
        }
    )
    SwipeToDismissBox(state = swipeToDismissState,
        backgroundContent = {
            val backgroundColor by animateColorAsState(
                targetValue = when(swipeToDismissState.currentValue){
                    SwipeToDismissBoxValue.Settled ->  MaterialTheme.colorScheme.surface
                    SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.error
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                },
                label = "Background Color"
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            )
        },
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clickable {
                    onNavigateToDetailScreen(game)
                }
                .padding(4.dp),
            colors = cardColor()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                NetworkImage(
                    url = game.backgroundImage ?: "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(80.dp)
                        .clip(RoundedCornerShape(5.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        text = game.name ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${game.rating}/5",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        game.released?.let {
                            Text(
                                text = it.convertDateTo(ConverterDate.FULL_DATE),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}