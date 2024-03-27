package dev.robert.search.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.database.database.GamesDatabase
import dev.robert.network.apiservice.GamesApi
import dev.robert.search.data.repository.SearchRepositoryImpl
import dev.robert.search.domain.repository.SearchRepository
import dev.robert.search.domain.usecase.AddToRecentSearchUseCase
import dev.robert.search.domain.usecase.GetRecentSearchesUseCase
import dev.robert.search.domain.usecase.SearchUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Provides
    @Singleton
    fun provideSearchRepository(
        api: GamesApi,
        db: GamesDatabase
    ): SearchRepository = SearchRepositoryImpl(api, db)


    @Provides
    @Singleton
    fun provideSearchUseCase(
        repository: SearchRepository
    ) = SearchUseCase(repository)
    @Provides
    @Singleton
    fun provideGetRecentSearchUseCase(
        repository: SearchRepository
    ) = GetRecentSearchesUseCase(repository)

    @Provides
    @Singleton
    fun provideAddTo(
        repository: SearchRepository
    ) = AddToRecentSearchUseCase(repository)
}