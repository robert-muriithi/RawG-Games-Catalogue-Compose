package dev.robert.favorites.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.database.database.GamesDatabase
import dev.robert.favorites.data.repository.BookmarksRepositoryImpl
import dev.robert.favorites.domain.repository.BookmarksRepository
import dev.robert.favorites.domain.usecase.ClearBookmarksUseCase
import dev.robert.favorites.domain.usecase.DeleteBookmarkUseCase
import dev.robert.favorites.domain.usecase.GetBookmarksUseCase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object BookmarksModule {

    @Provides
    @Singleton
    fun provideBookmarksRepository(
        database: GamesDatabase
    ) : BookmarksRepository {
        return BookmarksRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideGetBookmarksUseCase(
        repository: BookmarksRepository
    ) : GetBookmarksUseCase {
        return GetBookmarksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideClearBookmarksUseCase(
        repository: BookmarksRepository
    ) : ClearBookmarksUseCase {
        return ClearBookmarksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteBookmarkUseCase(
        repository: BookmarksRepository
    ) : DeleteBookmarkUseCase {
        return DeleteBookmarkUseCase(repository)
    }
}