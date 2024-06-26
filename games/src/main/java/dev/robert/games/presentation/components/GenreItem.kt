package dev.robert.games.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.robert.designsystem.presentation.NetworkImage
import dev.robert.games.domain.model.genre.Genre

@Composable
fun GenreItem(
    genre: Genre,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clickable {
                onClick(genre.name)
            }
            .shadow(4.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(MaterialTheme.shapes.medium)
            ) {
                NetworkImage(
                    url = genre.imageBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentDescription = genre.name
                )
                Text(
                    text = genre.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(4.dp)
                        .align(Alignment.BottomCenter)

                )
            }
    }
}