package dev.robert.datastore.utils

import androidx.datastore.preferences.core.intPreferencesKey

object Constants {
    const val GAME_TRAIL_DATA_STORE = "game_trail_data_store"
    val THEME_OPTIONS = intPreferencesKey(name = "theme_option")
}