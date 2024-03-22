package dev.robert.database.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.robert.database.converters.GameEntityConverters
import dev.robert.database.dao.GenreEntityDao
import dev.robert.database.dao.GameDao
import dev.robert.database.dao.RemoteKeyDao
import dev.robert.database.database.GamesDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGson () = Gson()

    @Provides
    @Singleton
    fun provideGameEntityDao(
        database: GamesDatabase,
    ): GameDao = database.gameEntityDao()

    @Provides
    @Singleton
    fun provideRemoteKeyDao(
        database: GamesDatabase,
    ): RemoteKeyDao = database.remoteKeyDao()

    @Provides
    @Singleton
    fun provideCategoriesDao(
        database: GamesDatabase,
    ): GenreEntityDao = database.genreEntityDao()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        converters: GameEntityConverters
    ): GamesDatabase = Room.databaseBuilder(
        context,
        GamesDatabase::class.java,
        "games_database",
    ).addTypeConverter(converters).build()

    @Singleton
    @Provides
    fun provideProductEntityConverters(gson: Gson): GameEntityConverters = GameEntityConverters(gson)
}