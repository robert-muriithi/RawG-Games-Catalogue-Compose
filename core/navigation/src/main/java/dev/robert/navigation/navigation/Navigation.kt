package dev.robert.navigation.navigation

import dev.robert.shared.R

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val iconOutlined: Int? = null,
    val iconFilled: Int? = null
) {
     data object HomeScreen : Destinations(
        route = "home_screen",
        title = "Home",
        iconOutlined = R.drawable.home_outline,
        iconFilled = R.drawable.home_filled
    )

    data object SearchScreen : Destinations(
        route = "search_screen",
        title = "Search",
        iconOutlined = R.drawable.search,
        iconFilled = R.drawable.search,
    )
    data object BookMarksScreen : Destinations(
        route = "bookmarks_screen",
        title = "Bookmarks",
        iconOutlined = R.drawable.bookmark_outline,
        iconFilled = R.drawable.bookmark_filled
    )

    data object SettingsScreen : Destinations(
        route = "settings_screen",
        title = "Settings",
        iconOutlined = R.drawable.settings_outline,
        iconFilled = R.drawable.settings_filled
    )

    data object GameDetailsScreen : Destinations(
        route = "detail_screen"
    )

    data object GameCategoriesScreen : Destinations(
        route = "category_screen"
    )

    data object GenreDetailsScreen : Destinations(
        route = "genre_details_screen"
    )

}