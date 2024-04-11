package dev.robert.settings.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    Scaffold (
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            MediumTopAppBar(title = {
                Text("Settings")
            })
        }
    ){ paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Text("Not implemented", modifier = Modifier.align(Alignment.Center))
        }
    }
}