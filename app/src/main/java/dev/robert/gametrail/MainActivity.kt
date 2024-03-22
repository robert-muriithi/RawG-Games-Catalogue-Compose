package dev.robert.gametrail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.favorites.presentation.BookmarksScreen
import dev.robert.games.presentation.GameDetailsScreen
import dev.robert.games.presentation.HomeScreen
import dev.robert.gametrail.ui.theme.GameTrailTheme
import dev.robert.navigation.navigation.Destinations
import dev.robert.search.presentation.SearchScreen
import dev.robert.settings.presentation.SettingsScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val theme by viewModel.theme.collectAsStateWithLifecycle()
            GameTrailTheme(
                theme = theme
            ) {
                val navController: NavHostController = rememberNavController()
                val bottomBarHeight = 60.dp
                val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }

                val buttonsVisible = remember { mutableStateOf(true) }

                LaunchedEffect(bottomBarOffsetHeightPx.floatValue) {
                    buttonsVisible.value = bottomBarOffsetHeightPx.value >= -5
                }

                val shouldShouldBottomBar =
                    navController.currentBackStackEntryAsState().value?.destination?.route in
                            listOf(
                                Destinations.HomeScreen.route,
                                Destinations.SearchScreen.route,
                                Destinations.BookMarksScreen.route,
                                Destinations.SettingsScreen.route
                            )
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            if (shouldShouldBottomBar) {
                                BottomBar(
                                    navController = navController,
                                    state = buttonsVisible,
                                    modifier = Modifier,
                                )
                            }
                        }, modifier = Modifier.bottomBarAnimatedScroll(
                            height = bottomBarHeight, offsetHeightPx = bottomBarOffsetHeightPx
                        )
                    ) {
                        Box(modifier = Modifier.padding(it)) {
                            NavigationGraph(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.bottomBarAnimatedScroll(
    height: Dp = 56.dp, offsetHeightPx: MutableState<Float>,
): Modifier = composed {
    val bottomBarHeightPx = with(LocalDensity.current) {
        height.roundToPx().toFloat()
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = offsetHeightPx.value + delta
                offsetHeightPx.value = newOffset.coerceIn(-bottomBarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    this.nestedScroll(nestedScrollConnection)
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Destinations.HomeScreen.route) {
        composable(Destinations.HomeScreen.route) {
            HomeScreen(
                navController = navController
            )
        }
        composable(Destinations.SearchScreen.route) {
            SearchScreen(
                navController = navController
            )
        }
        composable(Destinations.BookMarksScreen.route) {
            BookmarksScreen(
                navController = navController
            )
        }
        composable(Destinations.SettingsScreen.route) {
            SettingsScreen(
                navController = navController
            )
        }
        composable(
            Destinations.GameDetailsScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
            )
        ) {
            val gameId = it.arguments?.getInt("id")
            gameId?.let { it1 ->
                GameDetailsScreen(
                    gameId = it1,
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    val screens = listOf(
        Destinations.HomeScreen,
        Destinations.SearchScreen,
        Destinations.BookMarksScreen,
        Destinations.SettingsScreen
    )
    AnimatedVisibility(
        visible = state.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        NavigationBar(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            screens.forEach { screen ->
                NavigationBarItem(
                    label = {
                        screen.title?.let { title ->
                            Text(text = title, color = currentRoute?.let {
                                if (it == screen.route) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            } ?: MaterialTheme.colorScheme.onSurface)
                        }
                    },
                    icon = {
                        screen.icon?.let {
                            Icon(imageVector = it,
                                contentDescription = ""
                            )
                        }
                    },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedTextColor = Color.Gray, selectedTextColor = Color.White
                    ),
                )
            }
        }
    }
}

