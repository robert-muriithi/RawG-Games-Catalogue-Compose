package dev.robert.games.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dev.robert.products.domain.model.Product
import dev.robert.games.presentation.components.CustomButton

interface ProductDetailsScreenNavigator {

}

@Composable
fun ProductDetailsScreen(
    product : Product,
    navigator: ProductDetailsScreenNavigator,
) {
    ProductDetailsComponent(product = product, navigator = navigator)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailsComponent(
    product : Product,
    navigator: ProductDetailsScreenNavigator,
) {
    Scaffold(
        topBar = {
        },
        bottomBar = {
            BottomAppBarComponent(
                product = product,
            )
        }
    ) {
        Box(modifier =  Modifier.fillMaxSize()){
            LazyColumn(content = {
                item {
                    ProductDetailsHeader(product = product)
                }
                item {
                    ProductDetailsDescription(product = product)
                }
            })
        }
    }
}

@Composable
fun BottomAppBarComponent(
    product: Product,
    onAddToCartClick : (Int) -> Unit = {},
) {
    BottomAppBar(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp) ) {
        Text(text = "$ ${product.price}", modifier = Modifier.weight(1f))
        CustomButton(text = "Add to cart", onClick = {
            onAddToCartClick(product.id)
        }, shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(0f) )
    }

}

@Composable
fun ProductDetailsHeader(
    product : Product
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = product.image)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
            }).build()
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

@Composable
fun ProductDetailsDescription(
    product : Product
) {
    Text(text = product.description, modifier = Modifier.fillMaxWidth(), style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Thin))
}