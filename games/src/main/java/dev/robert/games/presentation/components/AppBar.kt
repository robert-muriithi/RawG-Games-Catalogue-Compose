package dev.robert.products.presentation.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults.Elevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dev.robert.products.presentation.utils.ExitUntilCollapsedState
import dev.robert.products.presentation.utils.ToolbarState


private val MinToolbarHeight = 96.dp
private val MaxToolbarHeight = 176.dp

private val ContentPadding = 8.dp
private val Elevation = 4.dp
private val ButtonSize = 24.dp
private const val Alpha = 0.75f

private val ExpandedPadding = 1.dp
private val CollapsedPadding = 3.dp

private val ExpandedCostaRicaHeight = 20.dp
private val CollapsedCostaRicaHeight = 16.dp

private val ExpandedWildlifeHeight = 32.dp
private val CollapsedWildlifeHeight = 24.dp

private val MapHeight = CollapsedCostaRicaHeight * 2


@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUntilCollapsedState.Saver) {
        ExitUntilCollapsedState(toolbarHeightRange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCollapsibleToolbar(
    @DrawableRes backgroundImageResId: Int,
    @DrawableRes logoResId: Int,
    progress: Float,
    onSearchButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val costaRicaHeight = with(LocalDensity.current) {
        lerp(CollapsedCostaRicaHeight, ExpandedCostaRicaHeight, progress)
    }
    val wildlifeHeight = with(LocalDensity.current) {
        lerp(CollapsedWildlifeHeight, ExpandedWildlifeHeight, progress)
    }
    val logoPadding = with(LocalDensity.current) {
        lerp(CollapsedPadding, ExpandedPadding, progress)
    }
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = ContentPadding)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = backgroundImageResId),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = progress * Alpha
                    },
                alignment = BiasAlignment(0f, 1f - ((1f - progress) * 0.75f))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = ContentPadding),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchButton(onSearchButtonClicked)
                SettingsButton(onSettingsButtonClicked)
            }
            Image(
                painter = painterResource(id = logoResId),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = progress * Alpha
                    },
                alignment = BiasAlignment(0f, 1f - ((1f - progress) * 0.75f))
            )
        }
    }
}

private operator fun Any.rangeTo(maxToolbarHeight: Dp): IntRange {
    return IntRange(0, maxToolbarHeight.value.toInt())
}

@Composable
fun CollapsingToolBarLayout(
    progress: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

}

@Composable
fun SearchButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun SettingsButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
