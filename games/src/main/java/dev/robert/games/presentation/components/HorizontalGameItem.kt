package dev.robert.games.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.utils.ConverterDate
import dev.robert.games.utils.convertDateTo

@Composable
fun HorizontalGameItem(
    game: GamesResultModel,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                game.id?.let { onClick(it) }
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
                    .width(120.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(5.dp)
            ) {
                Text(
                    text = game.name ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
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
                Spacer(modifier = Modifier.height(4.dp))
                PlatformIcons(game = game)
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

@Composable
fun cardColor(): CardColors {
    return CardColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        disabledContentColor = MaterialTheme.colorScheme.onSurface,
    )
}