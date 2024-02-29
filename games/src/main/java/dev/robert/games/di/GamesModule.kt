package dev.robert.games.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.database.database.GamesDatabase
import dev.robert.games.data.repository.GamesRepositoryImpl
import dev.robert.games.domain.repository.GamesRepository
import dev.robert.games.domain.usecase.GetGenresUseCase
import dev.robert.games.domain.usecase.GetProductById
import dev.robert.games.domain.usecase.GetProductCategory
import dev.robert.games.domain.usecase.GetGamesUseCase
import dev.robert.network.apiservice.GamesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GamesModule {
    @Provides
    @Singleton
    fun provideGamesRepository(
        productApi: GamesApi,
        appDb: GamesDatabase
    ): GamesRepository = GamesRepositoryImpl(productApi, appDb)


    @Provides
    @Singleton
    fun provideGetProductsUseCase(
        repository: GamesRepository
    ): GetGamesUseCase = GetGamesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetProductByIdUseCase(
        repository: GamesRepository
    ): GetProductById = GetProductById(repository)
    @Provides
    @Singleton
    fun provideGetProductByCategoryUseCase(
        repository: GamesRepository
    ): GetProductCategory = GetProductCategory(repository)

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(
        repository: GamesRepository
    ): GetGenresUseCase = GetGenresUseCase(repository)


}