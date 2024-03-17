package dev.robert.navigation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
     data object HomeScreen : Destinations(
        route = "home_screen",
        title = "Home",
        icon = Icons.Outlined.Home
    )

    data object SearchScreen : Destinations(
        route = "search_screen",
        title = "Search",
        icon = Icons.Outlined.Search
    )
    data object BookMarksScreen : Destinations(
        route = "bookmarks_screen",
        title = "Bookmarks",
        icon = Icons.Outlined.Bookmarks
    )

    data object SettingsScreen : Destinations(
        route = "settings_screen",
        title = "Settings",
        icon = Icons.Outlined.Settings
    )

    data object GameDetailsScreen : Destinations(
        route = "detail_screen"
    )

    data object GameCategoriesScreen : Destinations(
        route = "category_screen"
    )

}