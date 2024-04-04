package dev.robert.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import dev.robert.designsystem.presentation.NetworkImage
import dev.robert.designsystem.presentation.cardColor
import dev.robert.search.R
import dev.robert.search.domain.model.Game
import dev.robert.shared.utils.ConverterDate
import dev.robert.shared.utils.convertDateTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    val viewModel: SearchViewModel = hiltViewModel()
    var searchString = viewModel.query.value

    val onQueryChanged: (String) -> Unit = { viewModel.onQueryChanged(it) }

    val onRecentSearchClicked: (Int, Boolean) -> Unit = { id, recentSearch ->
        viewModel.addToRecentSearch(id, recentSearch)
    }
    val searchGamesState = viewModel.gamesState
    val games = searchGamesState.value.data?.collectAsLazyPagingItems()
    val recentSearch =
        viewModel.recentSearches.value.data?.collectAsStateWithLifecycle(initialValue = null)
    val lazyListState = rememberLazyListState()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(title = {
                Text("Search")
            })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Search bar
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .shadow(elevation = 3.dp, shape = RoundedCornerShape(10.dp))
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {},
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .weight(5f)
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            value = searchString,
                            onValueChange = {
                                searchString = it
                                onQueryChanged(it)
                            },
                            enabled = true,
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            decorationBox = { innerTextField ->
                                if (searchString.isEmpty()) {
                                    Text(
                                        text = "Search",
                                        color = Color.Gray.copy(alpha = 0.5f),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                innerTextField()
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(onSearch = {
                                viewModel.search()
                            }),
                            singleLine = true,
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .size(50.dp)
                                .background(color = Color.Transparent, shape = CircleShape)
                                .clickable {
                                    if (searchString.isNotEmpty()) {
                                        onQueryChanged("")
                                        viewModel.clearSearchResults()
                                    }
                                },
                        ) {
                            if (searchString.isNotEmpty()) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.search_hint),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            } else {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search_hint),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
                if (searchGamesState.value.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        state = lazyListState,
                    ) {
                        games.let {
                            it?.let { pagingItems ->
                                items(pagingItems.itemCount) { index ->
                                    it[index]?.let { gameResult ->
                                        SearchGameItem(
                                            game = gameResult,
                                            onClick = { id ->
                                            },
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun SearchGameItem(
    game: Game,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                onClick(game.id)
            }
            .padding(4.dp),
        colors = cardColor(),
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
