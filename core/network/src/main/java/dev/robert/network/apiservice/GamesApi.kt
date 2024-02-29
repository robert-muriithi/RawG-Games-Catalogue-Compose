package dev.robert.network.apiservice

import dev.robert.games.data.dto.category.GameGenreResponseDto
import dev.robert.network.dto.dto.games.GamesResponseDto
import dev.robert.network.dto.dto.tags.TagsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GamesApi {
    @GET("genres")
    suspend fun getGameGenres(): GameGenreResponseDto
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("search") search: String? = null,
        @Query("search_exact") searchExact: Boolean? = null
    ): GamesResponseDto
    @GET("tags")
    suspend fun getTags(): TagsResponseDto
}