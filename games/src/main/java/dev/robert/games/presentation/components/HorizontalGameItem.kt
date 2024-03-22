package dev.robert.games.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.robert.games.domain.model.game.GamesResultModel

@Composable
fun HorizontalGameItem(
    game: GamesResultModel,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
)
{
    Column(
        modifier = modifier
            .width(140.dp)
            .clickable {
                game.id?.let { onClick(it) }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NetworkImage(
            url = game.backgroundImage ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentDescription = game.name ?: ""
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = game.name ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        RatingBar(
            rating = game.rating ?: 0.0,
            maxStars = 5,
            modifier = Modifier.height(10.dp)
        )
    }
}