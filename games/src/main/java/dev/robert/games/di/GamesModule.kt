package dev.robert.games.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.database.database.GamesDatabase
import dev.robert.games.data.repository.GamesRepositoryImpl
import dev.robert.games.domain.repository.GamesRepository
import dev.robert.games.domain.usecase.BookMarkGameUseCase
import dev.robert.games.domain.usecase.GetGameDetailsUseCase
import dev.robert.games.domain.usecase.GetGenresUseCase
import dev.robert.games.domain.usecase.GetGamesUseCase
import dev.robert.games.domain.usecase.GetGenreGamesUseCase
import dev.robert.games.domain.usecase.GetHotGamesUseCase
import dev.robert.games.domain.usecase.GetLocalGameUseCase
import dev.robert.games.domain.usecase.SearchGamesUseCase
import dev.robert.network.apiservice.GamesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GamesModule {
    @Provides
    @Singleton
    fun provideGamesRepository(
        api: GamesApi,
        appDb: GamesDatabase
    ): GamesRepository = GamesRepositoryImpl(api, appDb)


    @Provides
    @Singleton
    fun provideGetGamesUseCase(
        repository: GamesRepository
    ): GetGamesUseCase = GetGamesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetGenreGamesUseCase(
        repository: GamesRepository
    ): GetGenreGamesUseCase = GetGenreGamesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetHotGamesUseCase(
        repository: GamesRepository
    ): GetHotGamesUseCase = GetHotGamesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLocalGameUseCase(
        repository: GamesRepository
    ): GetLocalGameUseCase = GetLocalGameUseCase(repository)
    @Provides
    @Singleton
    fun provideSearchGamesUseCase(
        repository: GamesRepository
    ): SearchGamesUseCase = SearchGamesUseCase(repository)


    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(
        repository: GamesRepository
    ): GetGenresUseCase = GetGenresUseCase(repository)

    @Provides
    @Singleton
    fun provideGetGameDetailsUseCase(
        repository: GamesRepository
    ): GetGameDetailsUseCase = GetGameDetailsUseCase(repository)

    @Provides
    @Singleton
    fun provideBookMarkGameUseCase(
        repository: GamesRepository
    ): BookMarkGameUseCase = BookMarkGameUseCase(repository)


    /*@OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideGamesPager(
        db: GamesDatabase,
        api: GamesApi
    ) : Pager<Int, GameEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = GamesRemoteMediator(
                apiService = api,
                appDb = db
            ),
            pagingSourceFactory = {
                db.gameEntityDao().getAllGames()
            }
        )
    }*/

    /*@OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideGenresPager(
        db: GamesDatabase,
        api: GamesApi
    ) : Pager<Int, GenreEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = GenresRemoteMediator(
                apiService = api,
                appDb = db
            ),
            pagingSourceFactory = {
                db.genreEntityDao().getAllGenres()
            }
        )
    }*/
}