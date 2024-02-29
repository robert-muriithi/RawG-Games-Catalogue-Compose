package dev.robert.products.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.robert.games.presentation.ProductsViewModel
import dev.robert.products.R
import dev.robert.products.domain.model.Product
import dev.robert.products.domain.model.Rating
import dev.robert.products.presentation.destinations.ProductDetailsScreenDestination
import dev.robert.products.presentation.utils.ExitUntilCollapsedState
import dev.robert.products.presentation.utils.FixedScrollFlagState
import dev.robert.products.presentation.utils.ToolbarState
import java.lang.Math.ceil
import java.lang.Math.floor


@Composable
@Destination
@RootNavGraph(start = true)
fun HomeScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
    navigator: HomeScreenNavigator
) {
    val productsState = viewModel.productsState.value
    val categoriesState = viewModel.categoriesState.value
    val selectedCategory = viewModel.selectedCategory.value
    val products = viewModel.products.value

    val verticalGridState = rememberLazyStaggeredGridState()

    /*val onProductSelected: (Int) -> Unit = { productId ->
        navigator.navigate(
            ProductDetailsScreenDestination.invoke(productsState.data?.find { it.id == productId }!!)
        )
    }*/

    val onProductCategorySelected: (String) -> Unit = { category ->
        viewModel.setCategory(category)
    }

    ProductsWidget(
        productsState = productsState,
        products = products,
        categoriesState = categoriesState,
//        selectedCategory = selectedCategory,
//        onCategorySelected = viewModel::setCategory,
        verticalGridState = verticalGridState,
        navigator = navigator,
        modifier = Modifier,
//        onProductSelected = onProductSelected,
        onProductCategorySelected = onProductCategorySelected
    )
}

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUntilCollapsedState.Saver) {
        ExitUntilCollapsedState(toolbarHeightRange)
    }
}
private  val MinToolbarHeight = 96.dp
private  val MaxToolbarHeight = 176.dp
@Composable
fun ProductsWidget(
    productsState: StateHolder<List<Product>>?,
    products: StateHolder<List<Product>>?,
    categoriesState: StateHolder<List<String>>?,
    verticalGridState : LazyStaggeredGridState,
    navigator: HomeScreenNavigator,
    modifier: Modifier = Modifier,
//    onProductSelected: (Int) -> Unit,
    onProductCategorySelected: (String) -> Unit,
) {
    val toolbarHeightRange = with(LocalDensity.current) {
        MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val listState = rememberLazyStaggeredGridState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached = listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(0f, toolbarState.consumed)
            }
        }
    }

    Box(modifier = modifier.nestedScroll(nestedScrollConnection)) {
        LazyProducts(
            products = products,
//            onProductSelected = onProductSelected,
            onProductCategorySelected = onProductCategorySelected,
            verticalGridState = verticalGridState,
            navigator = navigator,
            contentPaddingValues = PaddingValues(bottom = if (toolbarState is FixedScrollFlagState) MinToolbarHeight else 0.dp)
        )
        /*HomeCollapsibleToolbar(
            backgroundImageResId = R.drawable.banner,
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
fun LazyProducts(
    products: StateHolder<List<Product>>?,
//    onProductSelected: (Int) -> Unit,
    onProductCategorySelected: (String) -> Unit,
    verticalGridState : LazyStaggeredGridState,
    navigator: HomeScreenNavigator,
    contentPaddingValues: PaddingValues = PaddingValues(8.dp),
) {
    val context = LocalContext.current
    val contentPadding = PaddingValues(8.dp)
    LazyVerticalStaggeredGrid(
        contentPadding = contentPadding,
        state = verticalGridState,
        modifier = Modifier.fillMaxSize().padding(top = 20.dp),
        columns = StaggeredGridCells.Adaptive(150.dp),
        content = {
            products?.data?.let {
                items(it.size) { index ->
                    ProductCard(
                        product = products.data[index],
                        /*onProductSelected = {
                            navigator.navigate(
                                ProductDetailsScreenDestination.invoke(products.data[index])
                            )
                        },*/
//                        onProductCategorySelected = onProductCategorySelected,
                        onclick = {
                            navigator.openProductDetails(products.data[index])
                        }
                    )
                }
            }
        }
    )
}



/*@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductsWidget(
    productsState: StateHolder<List<Product>>?,
    products: StateHolder<List<Product>>?,
    categoriesState: StateHolder<List<String>>?,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    verticalGridState : LazyStaggeredGridState,
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            HomeCollapsibleToolbar(
                backgroundImageResId = R.drawable.banner,
                progress = 0f,
                onSearchButtonClicked = {},
                onSettingsButtonClicked = {}
            )
        },
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp)) {
            if (products!!.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
                    navigator = navigator
                )
        }
    }


}*/

@Composable
fun ProductSuccessComponent(
    productsState: StateHolder<List<Product>>?,
    selectedCategory: String,
    products: StateHolder<List<Product>>?,
    onProductSelected: (Int) -> Unit,
    onProductCategorySelected: (String) -> Unit,
    verticalGridState : LazyStaggeredGridState,
    navigator: DestinationsNavigator
) {
    val data = products?.data
    if (products == null) return
    if (data == null) return
    if (products.isLoading || products.error != null || data.isEmpty()) return
    when {
        selectedCategory != "All" -> ProductsList(
            products = productsState,
            onProductSelected = onProductSelected,
            onProductCategorySelected = onProductCategorySelected,
            verticalGridState = verticalGridState,
            navigator = navigator
        )
        else -> Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)) {
            ProductsList(
                products = products,
                onProductSelected = onProductSelected,
                onProductCategorySelected = onProductCategorySelected,
                verticalGridState = verticalGridState,
                navigator = navigator
            )
        }
    }
}

@Composable
fun BoxScope.ErrorComponent(
    productsState: StateHolder<List<Product>>?
) {
    productsState?.error?.let {
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
    categories: StateHolder<List<String>>?,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier : Modifier = Modifier
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
    products: StateHolder<List<Product>>?,
    onProductSelected: (Int) -> Unit,
    onProductCategorySelected: (String) -> Unit,
    verticalGridState : LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
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
            products?.data?.let {
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
            }
        }
    )

}

@Composable
fun HeaderItem() {
    Box(modifier = Modifier.fillMaxWidth()){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner),
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
    product: Product,
//    onProductSelected: (Int) -> Unit,
//    onProductCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    onclick: (Int) -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable {
                onclick(product.id)
            }) {
        Column(modifier = modifier.fillMaxWidth()) {
            ProductImage(
                imageUrl = product.image,
                contentDescription = product.title
            )
            Column(modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                ProductTitle(title = product.title)
                ProductDescription(description = product.description)
                ProductPriceAndRating(price = product.price, rating = product.rating)
                CartButton(onclick = {
                    onclick(product.id)
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
fun ProductRating(rating: Rating, starsColor : Color = Color.Yellow, modifier: Modifier = Modifier) {
    val filledStars = floor(rating.rate).toInt()
    val unfilledStars = (rating.count - ceil(rating.rate)).toInt()
    val halfStar = !(rating.rate.rem(1).equals(0.0))
     Row(
         modifier = modifier
     ){
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
    modifier: Modifier = Modifier
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
    contentDescription: String
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
interface HomeScreenNavigator {
    fun openHome()

    fun openProductDetails(product: Product)

    fun popupBackStack()
}

