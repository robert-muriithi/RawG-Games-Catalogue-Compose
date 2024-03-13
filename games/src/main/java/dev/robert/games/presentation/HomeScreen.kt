package dev.robert.games.presentation

import android.annotation.SuppressLint
import android.widget.RatingBar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.genre.Genre
import dev.robert.games.presentation.components.GameItem
import dev.robert.games.presentation.components.GenreItem
import dev.robert.games.presentation.components.NetworkImage
import dev.robert.games.utils.stringToColor
import dev.robert.products.domain.model.Product
import dev.robert.products.domain.model.Rating
import dev.robert.products.presentation.utils.ExitUntilCollapsedState
import dev.robert.products.presentation.utils.ToolbarState
import dev.robert.products.presentation.widgets.HomeCollapsibleToolbar
import dev.robert.shared.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.lang.Math.ceil
import java.lang.Math.floor




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateToDetails : (GamesResultModel) -> Unit
) {
    val genresState = viewModel.genresState.value
    val gamesState = viewModel.gamesState.value
    val hotGamesState = viewModel.hotGamesState.value
    val selectedCategory = viewModel.selectedCategory.value

    val games = gamesState.data?.collectAsLazyPagingItems()
    val genres = genresState.data?.collectAsLazyPagingItems()
    val hotGames = hotGamesState.data


    val verticalGridState = rememberLazyGridState()



    Scaffold(
        /*topBar = {
            TopBar(
                genreState = genresState,
                selectedCategory = selectedCategory,
                onCategorySelected = viewModel::setCategory,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }*/
    ) {
        GamesWidget(
            gamesState = gamesState,
            games = games,
            hotGames = hotGames,
            categoriesState = genresState,
            genres = genres,
            verticalGridState = verticalGridState,
            modifier = Modifier,
            onGameSelected = {
                viewModel.onGameSelected(it)
            },
            onGenreSelected = { genre ->
                viewModel.setCategory(genre.name)
            },
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
    onGameSelected: (GamesResultModel) -> Unit,
    onGenreSelected: (Genre) -> Unit,
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

    Box(/*modifier = modifier.nestedScroll(nestedScrollConnection)*/) {
        LazyGames(
            lazyGames = games,
            hotGames = hotGames,
            verticalGridState = verticalGridState,
            onGameSelected = onGameSelected,
            genres = genres,
            onGenreSelected = onGenreSelected,
            // contentPaddingValues = PaddingValues(bottom = if (toolbarState is FixedScrollFlagState) MinToolbarHeight else 0.dp)
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
    onGameSelected: (GamesResultModel) -> Unit,
    lazyGames: LazyPagingItems<GamesResultModel>?,
    verticalGridState: LazyGridState,
    genres: LazyPagingItems<Genre>?,
    onGenreSelected: (Genre) -> Unit,
    hotGames: List<GamesResultModel>?,
) {
    val contentPadding = PaddingValues(8.dp)
    LazyVerticalGrid(
        userScrollEnabled = true,
        contentPadding = contentPadding,
        state = verticalGridState,
        modifier = Modifier.fillMaxSize(),
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
                    modifier = Modifier.width(180.dp),
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
                    Text(
                        text = "All Games",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            lazyGames.let {
                it?.let { pagingItems ->
                    items(pagingItems.itemCount) { index ->
                        it[index]?.let { gameResult ->
                            GameItem(
                                game = gameResult,
                                onClick = onGameSelected,
                            )
                        }
                    }
                }
            }
            when {
                lazyGames?.loadState?.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                lazyGames?.loadState?.append is LoadState.Error -> {
                    val e = lazyGames.loadState.append as LoadState.Error
                    item {
                        Text(text = e.error.localizedMessage ?: "Unknown error")
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
    onGameSelected: (GamesResultModel) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = {
        games?.size ?: 0
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
            FilterButton(
                onFilter = {},
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LaunchedEffect(key1 = pagerState, block = {
            snapshotFlow { pagerState.currentPage }.collect {
                pagerState.animateScrollToPage(it)
            }
        })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            games?.let {
                HotGameItem(
                    game = it[page],
                    onClick = onGameSelected,
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
fun FilterButton(
    onFilter: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = {}
    ) {
        Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = null
        )
    }
}

@Composable
fun HotGameItem(
    game: GamesResultModel,
    onClick: (GamesResultModel) -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 5.dp)
            .clickable {
                onClick(game)
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
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                    LazyRow(
                        content = {
                            game.genres?.let {
                                items(it.size) { index ->
                                    Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .background(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.shapes.small
                                            )
                                            .padding(4.dp)
                                    ) {
                                        Text(
                                            text = game.genres[index].name,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            }
                        },
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    maxStars: Int = 5,
    starsColor: Color = Color.Yellow,
) {
    val filledStars = kotlin.math.floor(rating).toInt()
    val unfilledStars = (maxStars - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))
    Row(modifier = modifier.padding(horizontal = 5.dp, vertical = 5.dp)) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor, modifier = Modifier.size(16.dp))
        }
        if (halfStar) {
            Icon(
                imageVector = Icons.Outlined.StarHalf,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(16.dp)
            )
        }
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun CategoriesWidget(
    genres: LazyPagingItems<Genre>?,
    onGenreSelected: (Genre) -> Unit = {},
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductsWidget(
    productsState: StateHolder<Flow<PagingData<GamesResultModel>>>,
    products: StateHolder<Flow<PagingData<GamesResultModel>>>,
    categoriesState: StateHolder<List<String>>?,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    verticalGridState: LazyStaggeredGridState,
) {
    Scaffold(
        topBar = {
            HomeCollapsibleToolbar(
                backgroundImageResId = dev.robert.shared.R.drawable.ic_android_black_24dp,
                progress = 0f,
                onSearchButtonClicked = {},
                onSettingsButtonClicked = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                logoResId = dev.robert.shared.R.drawable.ic_android_black_24dp
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {
            /*if (products!!.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            if (!products.isLoading && products.error != null) ErrorComponent(productsState = productsState)
            if (products.data?.isEmpty() == true && products.isLoading.not()) EmptyProductsComponent()
            if (products.data?.isNotEmpty() == true && products.isLoading.not() && products.error == null)
                ProductSuccessComponent(
                    productsState = productsState,
                    selectedCategory = selectedCategory,
                    products = products,
                    onProductSelected = {},
                    onProductCategorySelected = {},
                    verticalGridState = verticalGridState,
                )*/
        }
    }
}

@Composable
fun ProductSuccessComponent(
    productsState: StateHolder<Flow<PagingData<GamesResultModel>>>,
    selectedCategory: String,
    products: StateHolder<List<Product>>?,
    onProductSelected: (Int) -> Unit,
    onProductCategorySelected: (String) -> Unit,
    verticalGridState: LazyStaggeredGridState,
//    navigator: DestinationsNavigator
) {
    val data = products?.data
    if (products == null) return
    if (data == null) return
    if (products.isLoading || products.error != null || data.isEmpty()) return
    /*when {
        selectedCategory != "All" -> ProductsList(
            products = productsState,
            onProductSelected = onProductSelected,
            onProductCategorySelected = onProductCategorySelected,
            verticalGridState = verticalGridState,
//            navigator = navigator
        )
        else -> Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)) {
            ProductsList(
                products = products,
                onProductSelected = onProductSelected,
                onProductCategorySelected = onProductCategorySelected,
                verticalGridState = verticalGridState,
//                navigator = navigator
            )
        }
    }*/
}

@Composable
fun BoxScope.ErrorComponent(
    productsState: StateHolder<Flow<PagingData<GamesResultModel>>>,
) {
    productsState.error?.let {
        Text(
            text = it,
            modifier = Modifier.align(Alignment.Center)
        )
    }

}

@Composable
fun BoxScope.EmptyProductsComponent() {
    Text(
        text = "No products found",
        modifier = Modifier.align(Alignment.Center)
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

@Composable
fun ProductsList(
    products: LazyPagingItems<GamesResultModel>?,
    productsState: StateHolder<Flow<PagingData<GamesResultModel>>>?,
    onProductSelected: (Int) -> Unit,
    onProductCategorySelected: (String) -> Unit,
    verticalGridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val contentPadding = PaddingValues(8.dp)
    LazyVerticalStaggeredGrid(
        contentPadding = contentPadding,
        state = verticalGridState,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        columns = StaggeredGridCells.Adaptive(150.dp),
        content = {
            item {
                HeaderItem()
            }
            products.let {
                it?.let { pagingItems ->
                    items(pagingItems.itemCount) { index ->
                        it[index]?.let { gameResult ->
                            ProductCard(
                                product = gameResult,
                                onclick = {
                                    /*navigator.navigate(
                                                            ProductDetailsScreenDestination.invoke(it[index])
                                                        )*/
                                }
                            )
                        }
                    }
                }
            }
            when {
                products?.loadState?.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }

                products?.loadState?.append is LoadState.Error -> {
                    val e = products.loadState.append as LoadState.Error
                    item {
                        Text(text = e.error.localizedMessage ?: "Unknown error")
                    }
                }
            }
            /*products?.data?.let {
                items(it.size) { index ->
                    ProductCard(
                        product = products.data[index],
//                        onProductSelected = onProductSelected,
//                        onProductCategorySelected = onProductCategorySelected,
                        onclick = {
                            navigator.navigate(
                                ProductDetailsScreenDestination.invoke(products.data[index])
                            )
                        }
                    )
                }
            }*/
        }
    )

}

@Composable
fun HeaderItem() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Image(
                painter = painterResource(id = android.R.drawable.arrow_down_float),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ProductCard(
    product: GamesResultModel,
//    onProductSelected: (Int) -> Unit,
//    onProductCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    onclick: (Int) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable {
                onclick(product.id ?: 0)
            }) {
        Column(modifier = modifier.fillMaxWidth()) {
            ProductImage(
                imageUrl = product.backgroundImage ?: "",
                contentDescription = product.name ?: ""
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                ProductTitle(title = product.name ?: "")
                ProductDescription(description = product.metacritic.toString())
//                ProductPriceAndRating(price = product.price, rating = product.rating)
                CartButton(onclick = {
                    onclick(product.id ?: 0)
                })
            }
        }
    }
}

@Composable
fun CartButton(onclick: () -> Unit) {
    val clicked = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = {
                onclick()
                clicked.value = !clicked.value
            }) {
            Icon(
                imageVector = clicked.value.let {
                    if (it) Icons.Outlined.ShoppingCart else Icons.Default.ShoppingCart
                },
                contentDescription = "Add to cart",
                modifier = Modifier
                    .height(14.dp)

            )
        }
    }
}

@Composable
fun ProductPriceAndRating(price: Double, rating: Rating) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProductPriceTag(price = price, rating = rating)
//        ProductRating(rating = rating)
    }
}

@Composable
fun ProductRating(rating: Rating, starsColor: Color = Color.Yellow, modifier: Modifier = Modifier) {
    val filledStars = floor(rating.rate).toInt()
    val unfilledStars = (rating.count - ceil(rating.rate)).toInt()
    val halfStar = !(rating.rate.rem(1).equals(0.0))
    Row(
        modifier = modifier
    ) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = starsColor)
        }
        if (halfStar) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}


@Composable
fun ProductPriceTag(price: Double, rating: Rating) {
    Text(
        text = "$$price",
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    )
}

@Composable
fun ProductDescription(description: String) {
    Text(
        text = description,
        style = TextStyle(
            fontSize = 10.sp,
            fontWeight = FontWeight.Thin
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ProductTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    )
}


@Composable
fun ProductImage(
    imageUrl: String,
    contentDescription: String,
) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
        ),
        contentDescription = contentDescription,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        contentScale = ContentScale.Crop
    )
}
/*interface HomeScreenNavigator {
    fun openHome()

    fun openProductDetails(product: Product)

    fun popupBackStack()
}*/
