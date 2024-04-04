package dev.robert.games.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.robert.designsystem.presentation.NetworkImage
import dev.robert.designsystem.presentation.cardColor
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.shared.utils.ConverterDate
import dev.robert.shared.utils.convertDateTo
import dev.robert.shared.utils.getPlatformIcon

@Composable
fun GameItem(
    game: GamesResultModel,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onBookMark : (Int, Boolean) -> Unit,
) {
    var isBookMarked by remember {
        mutableStateOf(game.isBookMarked)
    }

    Card(
        modifier = modifier
            .width(220.dp)
            .clickable {
                game.id?.let { onClick(it) }
            },
        colors = cardColor(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box {
                NetworkImage(
                    url = game.backgroundImage ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentDescription = game.name ?: "Game Image"
                )
                IconButton(
                    modifier = Modifier.
                    indication(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    )
                        .align(Alignment.TopStart),
                    onClick = {
                        isBookMarked = !isBookMarked
                        onBookMark(game.id!!, isBookMarked)
                }) {
                    Image(
                        painter = painterResource(id = if (isBookMarked == true) {
                            dev.robert.shared.R.drawable.bookmark_filled
                        } else {
                            dev.robert.shared.R.drawable.bookmark_outline
                        }),
                        contentDescription = "Bookmark",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
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
        Row(
            modifier = Modifier
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
                critic = game.metacritic.toString(),
                modifier = Modifier
            )
        }
        LineSeparator()
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
                                .padding(4.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        LineSeparator()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
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
        LineSeparator()
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
                            .padding(4.dp),
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
    HorizontalDivider(
        modifier = modifier
            .height(1.dp)
    )
}

@Composable
fun MetaCriticTag(
    critic: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(
                MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.3f),
                MaterialTheme.shapes.small
            )
            .padding(4.dp)
    ) {
        Text(
            text =  if (critic == "") "-" else critic,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
