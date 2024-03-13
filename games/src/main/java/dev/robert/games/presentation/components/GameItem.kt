package dev.robert.games.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.utils.ConverterDate
import dev.robert.games.utils.convertDateTo
import dev.robert.games.utils.getPlatformIcon

@Composable
fun GameItem(
    game: GamesResultModel,
    onClick: (GamesResultModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(220.dp)
            .clickable {
                onClick(game)
            },
        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
//            modifier = Modifier.background(Color.Green)
            modifier = Modifier.fillMaxWidth()
        ) {
            NetworkImage(
                url = game.backgroundImage ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentDescription = game.name ?: "Game Image"
            )
            GameCardContent(game = game)
        }
    }
}

@Composable
fun GameCardContent(
    modifier: Modifier = Modifier,
    game: GamesResultModel,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TitleSection(game = game)
        LineSeparator()
        PlatformIcons(game = game)
        LineSeparator()
        GameReleaseDate(game = game)
        LineSeparator()
        GameGenres(game = game)
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    game: GamesResultModel,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = game.name ?: "Hello",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(.5f)
        )
        Spacer(modifier = Modifier.size(8.dp))
        MetaCriticTag(
            critic = game.metacritic ?: 0,
            modifier = Modifier
        )
    }
}

@Composable
fun GameReleaseDate(
    game: GamesResultModel,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Release Date:",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.size(4.dp))
        game.released?.let {
            Text(
                text = it.convertDateTo(ConverterDate.FULL_DATE),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun GameGenres(
    game: GamesResultModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Genres:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        game.genres?.map { genre ->
            Text(
                text = genre.name,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun PlatformIcons(
    modifier: Modifier = Modifier,
    game: GamesResultModel,
) {
    LazyRow(
        content = {
            game.parentPlatforms?.let { platforms ->
                items(platforms.size) {
                    val platform = game.parentPlatforms[it].platform.name
                    Image(
                        painter = painterResource(id = getPlatformIcon(platform)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(4.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}

@Composable
fun LineSeparator(
    modifier: Modifier = Modifier,
) {


    Divider(
        modifier = modifier
            .height(1.dp)
    )

}

@Composable
fun RowScope.GameTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.weight(1f)
    )
}

@Composable
fun MetaCriticTag(
    critic: Int?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
            .padding(4.dp)
    ) {
        Text(
            text = critic.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
