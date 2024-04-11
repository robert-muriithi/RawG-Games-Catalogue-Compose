package dev.robert.games.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.genre.Genre
import dev.robert.games.presentation.components.GameItem
import dev.robert.games.presentation.components.GenreItem
import dev.robert.games.presentation.components.GenresRow
import dev.robert.games.presentation.components.HorizontalGameItem
import dev.robert.designsystem.presentation.NetworkImage
import dev.robert.games.presentation.components.RatingBar
import dev.robert.games.presentation.events.HomeScreenEvent
import dev.robert.shared.utils.ExitUntilCollapsedState
import dev.robert.shared.utils.ToolbarState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val genresState = viewModel.genresState.value
    val gamesState = viewModel.gamesState.value
    val hotGamesState = viewModel.hotGamesState.value
    val games = gamesState.data?.collectAsLazyPagingItems()
    val genres = genresState.data?.collectAsLazyPagingItems()
    val hotGames = hotGamesState.data
    val theme = viewModel.theme.collectAsStateWithLifecycle()

    val verticalGridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.setNavController(navController)
    })


    val pullToRefreshState = rememberPullToRefreshState()
    /*LaunchedEffect(key1 = pullToRefreshState, block = {
        when (pullToRefreshState.isRefreshing) {
            true -> {
                viewModel.getHotGames(refresh = true)
                viewModel.getGames()
            }

            else -> {
                viewModel.getHotGames(refresh = false)
            }
        }
    })*/

    val showScrollToTopButton by remember {
        derivedStateOf {
            verticalGridState.firstVisibleItemIndex >= 5
        }
    }

    val lazyListState = rememberLazyListState()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = {
            if (showScrollToTopButton) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            verticalGridState.scrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) {
        GamesWidget(
            gamesState = gamesState,
            games = games,
            hotGames = hotGames,
            categoriesState = genresState,
            genres = genres,
            verticalGridState = verticalGridState,
            lazyListState = lazyListState,
            modifier = Modifier.padding(it),
            onGameSelected = {
                viewModel.onEvent(
                    HomeScreenEvent.NavigateToGameDetails(it)
                )
            },
            onGenreSelected = { genre ->
                viewModel.onEvent(
                    HomeScreenEvent.NavigateToGenreDetails(genre)
                )
            },
            onNavigateToSearch = {
                viewModel.onEvent(
                    HomeScreenEvent.NavigateToSearch
                )
            },
            onRefresh = {
                viewModel.getHotGames(refresh = true)
            },
            onBookMark = { id, bookmarked ->
                viewModel.onEvent(
                    HomeScreenEvent.BookMarkGame(id, bookmarked)
                )
            },
            theme = theme.value,
            onToggleTheme = { themeValue ->
                viewModel.onEvent(
                    HomeScreenEvent.ToggleTheme(themeValue)
                )
            }
        )
    }
}

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUntilCollapsedState.Saver) {
        ExitUntilCollapsedState(toolbarHeightRange)
    }
}

private val MinToolbarHeight = 96.dp
private val MaxToolbarHeight = 176.dp

@Composable
fun GamesWidget(
    gamesState: StateHolder<Flow<PagingData<GamesResultModel>>>,
    games: LazyPagingItems<GamesResultModel>?,
    hotGames: List<GamesResultModel>?,
    categoriesState: StateHolder<Flow<PagingData<Genre>>>,
    genres: LazyPagingItems<Genre>?,
    verticalGridState: LazyGridState,
//    navigator: HomeScreenNavigator,
    modifier: Modifier = Modifier,
    onGameSelected: (Int) -> Unit,
    onGenreSelected: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onRefresh: () -> Unit,
    lazyListState: LazyListState,
    onBookMark : (Int, Boolean) -> Unit,
    theme: Int,
    onToggleTheme: (Int) -> Unit
) {
    val toolbarHeightRange = with(LocalDensity.current) {
        MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val listState = rememberLazyStaggeredGridState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached =
                    listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(0f, toolbarState.consumed)
            }
        }
    }

    Box( modifier = modifier
    /*modifier = modifier.nestedScroll(nestedScrollConnection)*/) {
        LazyGames(
            lazyGames = games,
            hotGames = hotGames,
            verticalGridState = verticalGridState,
            onGameSelected = onGameSelected,
            genres = genres,
            onGenreSelected = onGenreSelected,
            onNavigateToSearch = onNavigateToSearch,
            onRefresh = onRefresh,
            lazyListState = lazyListState,
            onBookMark = onBookMark,
            theme = theme,
            onToggleTheme = onToggleTheme
        )
        /*HomeCollapsibleToolbar(
            backgroundImageResId = dev.robert.shared.R.drawable.ic_android_black_24dp,
            logoResId = android.R.drawable.ic_menu_search,
            progress = toolbarState.progress,
            onSearchButtonClicked = {

            },
            onSettingsButtonClicked = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                .graphicsLayer { translationY = toolbarState.offset }
        )*/
    }
}

@Composable
fun LazyGames(
    onGameSelected: (Int) -> Unit,
    lazyGames: LazyPagingItems<GamesResultModel>?,
    verticalGridState: LazyGridState,
    genres: LazyPagingItems<Genre>?,
    onGenreSelected: (String) -> Unit,
    hotGames: List<GamesResultModel>?,
    onNavigateToSearch: () -> Unit,
    onRefresh: () -> Unit,
    lazyListState: LazyListState,
    onBookMark: (Int, Boolean) -> Unit,
    theme: Int,
    onToggleTheme: (Int) -> Unit
) {
    val contentPadding = PaddingValues(8.dp)
    var showColumn by remember {
        mutableStateOf(true)
    }

    val layoutIcon = if (showColumn) Icons.Default.GridView else Icons.AutoMirrored.Filled.List
    val scope = rememberCoroutineScope()

    if (showColumn)
        LazyVerticalGrid(
            userScrollEnabled = true,
            contentPadding = contentPadding,
            state = verticalGridState,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            columns = GridCells.Adaptive(150.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                item(
                    span = {
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    HotGames(
                        games = hotGames,
                        onGameSelected = onGameSelected,
                        modifier = Modifier.fillMaxWidth(),
                        onNavigateToSearch = onNavigateToSearch,
                        theme= theme,
                        onToggleTheme = onToggleTheme
                    )
                }
                item(
                    span = {
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Categories",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.padding(8.dp)
                        )
                        CategoriesWidget(
                            genres = genres,
                            onGenreSelected = onGenreSelected
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "All Games",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        when (showColumn) {
                                            true -> {
                                                verticalGridState.scrollToItem(
                                                    lazyListState.firstVisibleItemIndex,
                                                    lazyListState.firstVisibleItemScrollOffset
                                                )
                                            }

                                            false -> {
                                                lazyListState.scrollToItem(
                                                    verticalGridState.firstVisibleItemIndex,
                                                    verticalGridState.firstVisibleItemScrollOffset
                                                )
                                            }
                                        }
                                    }
                                    showColumn = showColumn.not()
                                }
                            ) {
                                Icon(
                                    imageVector = layoutIcon,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
                lazyGames.let {
                    it?.let { pagingItems ->
                        items(pagingItems.itemCount) { index ->
                            it[index]?.let { gameResult ->
                                GameItem(
                                    game = gameResult,
                                    onClick = onGameSelected,
                                    onBookMark = onBookMark,
                                )
                            }
                        }
                    }
                }
            }
        )
    else
        LazyColumn(
            state = lazyListState,
            contentPadding = contentPadding,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                item {
                    HotGames(
                        games = hotGames,
                        onGameSelected = onGameSelected,
                        modifier = Modifier.fillMaxWidth(),
                        onNavigateToSearch = onNavigateToSearch,
                        theme = theme,
                        onToggleTheme = onToggleTheme
                    )
                }
                item {
                    Text(
                        text = "Categories",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                    CategoriesWidget(
                        genres = genres,
                        onGenreSelected = onGenreSelected
                    )
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "All Games",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.padding(8.dp)
                        )
                        IconButton(
                            onClick = {
                                scope.launch {
                                    when (showColumn) {
                                        true -> {
                                            verticalGridState.scrollToItem(
                                                lazyListState.firstVisibleItemIndex,
                                                lazyListState.firstVisibleItemScrollOffset
                                            )
                                        }

                                        false -> {
                                            lazyListState.scrollToItem(
                                                verticalGridState.firstVisibleItemIndex,
                                                verticalGridState.firstVisibleItemScrollOffset
                                            )
                                        }
                                    }
                                }
                                showColumn = showColumn.not()
                            }
                        ) {
                            Icon(
                                imageVector = layoutIcon,
                                contentDescription = null
                            )
                        }
                    }
                }
                lazyGames.let {
                    it?.let { pagingItems ->
                        items(pagingItems.itemCount) { index ->
                            it[index]?.let { gameResult ->
                                HorizontalGameItem(game = gameResult, onClick = onGameSelected)
                            }
                        }
                    }
                }
            }
        )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HotGames(
    games: List<GamesResultModel>?,
    modifier: Modifier = Modifier,
    onGameSelected: (Int) -> Unit,
    onNavigateToSearch: () -> Unit,
    theme: Int,
    onToggleTheme: (Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {
        games?.size ?: 0
    })

    LaunchedEffect(key1 = pagerState, block = {
        snapshotFlow { pagerState.currentPage }.collect {
            pagerState.animateScrollToPage(it)
        }
    })

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Hot Games",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(8.dp)
            )
            ActionButtons(
                onNavigateToSearch = onNavigateToSearch,
                modifier = Modifier,
                theme = theme,
                onToggleTheme = onToggleTheme
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            games?.let {
                HotGameItem(
                    game = it[page],
                    onGameSelected = onGameSelected,
                )
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}


@Composable
fun ActionButtons(
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    theme: Int,
    onToggleTheme: (Int) -> Unit
) {
    var isDarkMode by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
    ) {
        IconButton(onClick = {
            onNavigateToSearch()
        }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        }
        IconButton(
            onClick = {
                isDarkMode = isDarkMode.not()
                onToggleTheme(
                    if (isDarkMode) 1 else 0
                )
            }
        ) {
            Icon(
                imageVector = if (isDarkMode) Icons.Filled.WbSunny else Icons.Outlined.WbSunny,
                contentDescription = null
            )
        }
    }
}

@Composable
fun HotGameItem(
    game: GamesResultModel,
    onGameSelected: (Int) -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 5.dp)
            .clickable {
                game.id?.let { onGameSelected(it) }
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NetworkImage(
                url = game.backgroundImage ?: "",
                modifier = Modifier
                    .fillMaxWidth(),
                contentDescription = game.name ?: "Game Image"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .height(100.dp)
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = game.name ?: "",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                    GenresRow(
                        game = game,
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.5f),
                                MaterialTheme.shapes.small
                            )
                            .padding(4.dp)
                    )
                    RatingBar(
                        rating = game.rating ?: 0.0,
                        maxStars = 5
                    )
                }
            }
        }
    }
}


@Composable
fun CategoriesWidget(
    genres: LazyPagingItems<Genre>?,
    onGenreSelected: (String) -> Unit = {},
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            genres?.let { genres ->
                items(genres.itemCount) { index ->
                    genres[index]?.let { genre ->
                        GenreItem(
                            genre = genre,
                            onClick = onGenreSelected,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    genreState: StateHolder<Flow<PagingData<Genre>>>,
) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth(),
        title = {
            /*categories?.data?.indexOf(selectedCategory)?.let {
                ScrollableTabRow(selectedTabIndex = it) {
                    categories.data.forEachIndexed { _, category ->
                        Tab(
                            selected = selectedCategory == category,
                            onClick = { onCategorySelected(category) },
                            text = {
                                Text(text = category.capitalize(Locale.ROOT))
                            }
                        )
                    }
                }
            }*/
            Text(
                text = "Hello, Robert",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .padding(start = 10.dp)
            )
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null
                )
            }
        }
    )
}

