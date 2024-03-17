package dev.robert.datastore.data.repository

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dev.robert.datastore.domain.repo.DataStoreRepo
import dev.robert.datastore.utils.Constants.THEME_OPTIONS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepoImpl (
    private val dataStore: DataStore<Preferences>,
): DataStoreRepo {
    override suspend fun saveTheme(theme: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_OPTIONS] = theme
        }
    }
    override fun getTheme(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[THEME_OPTIONS] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }
}