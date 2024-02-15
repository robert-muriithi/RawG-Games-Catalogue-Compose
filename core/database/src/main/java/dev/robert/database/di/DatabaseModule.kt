package dev.robert.database.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.robert.database.converters.ProductEntityConverters
import dev.robert.database.dao.CategoriesDao
import dev.robert.database.dao.GamesDao
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
    fun provideProductsDao(
        database: GamesDatabase,
    ): GamesDao {
        return database.gamesDao()
    }

    @Provides
    @Singleton
    fun provideCategoriesDao(
        database: GamesDatabase,
    ): CategoriesDao {
        return database.categoriesDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        converters: ProductEntityConverters
    ): GamesDatabase {
        return Room.databaseBuilder(
            context,
            GamesDatabase::class.java,
            "games_database",
        ).addTypeConverter(converters).build()
    }

    @Singleton
    @Provides
    fun provideProductEntityConverters(gson: Gson): ProductEntityConverters {
        return ProductEntityConverters(gson)
    }
}