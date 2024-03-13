package dev.robert.designsystem.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CustomSnackBar(
    message: String,
    action: (() -> Unit),
) {

    var counterState by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            counterState = false
            action()
        }
    }

    AnimatedVisibility(
        visible = counterState,
        enter = slideInVertically(
            initialOffsetY = { -40 },
            animationSpec = tween(300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -40 },
            animationSpec = tween(300)
        )
    ) {
        Card(
            modifier = Modifier
                .absoluteOffset(x = 0.dp, y = 10.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .statusBarsPadding()
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(10.dp)
                ),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = dev.robert.shared.R.drawable.ic_android_black_24dp),
                    contentDescription = message
                )
                Text(
                    text = message,
//                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
        }
    }
}