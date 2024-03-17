package dev.robert.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.robert.datastore.data.repository.DataStoreRepoImpl
import dev.robert.datastore.domain.repo.DataStoreRepo
import dev.robert.datastore.domain.usecase.ThemeUseCase
import dev.robert.datastore.utils.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(Constants.GAME_TRAIL_DATA_STORE)
            }
        )

    @Provides
    @Singleton
    fun provideDataStoreRepo(dataStore: DataStore<Preferences>): DataStoreRepo = DataStoreRepoImpl(dataStore)

    @Provides
    @Singleton
    fun provideThemeUseCase(dataStoreRepo: DataStoreRepo) = ThemeUseCase(dataStoreRepo)
}