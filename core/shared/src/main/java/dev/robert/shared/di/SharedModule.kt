package dev.robert.shared.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.database.database.GamesDatabase
import dev.robert.shared.ApiResponse
import dev.robert.shared.mediator.RemoteMediatorHelper
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SharedModule {
    @Provides
    fun <T : Any> provideRemoteMediatorHelper(
        appDb: GamesDatabase,
        @Named("entityClass") entityClass: Class<T>,
        apiCall: suspend (Int) -> ApiResponse
    ): RemoteMediatorHelper<T> {
        return RemoteMediatorHelper(appDb, entityClass, apiCall)
    }

    @Provides
    @Named("entityClass")
    fun provideEntityClass(): Class<*> {
        return Any::class.java
    }


}