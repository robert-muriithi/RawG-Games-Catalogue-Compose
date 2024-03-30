package dev.robert.games.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.robert.games.domain.model.game.GamesResultModel

@Composable
fun GenresRow(
    game: GamesResultModel,
    modifier: Modifier = Modifier
) {
    LazyRow(
        content = {
            game.genres?.let {
                items(it.size) { index ->
                    Box(
                        modifier = modifier

                    ) {
                        Text(
                            text = game.genres[index].name,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                        )
                    }
                }
            }
        },
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    )
}