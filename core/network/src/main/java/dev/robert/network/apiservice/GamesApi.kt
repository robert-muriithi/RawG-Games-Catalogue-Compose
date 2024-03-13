package dev.robert.network.apiservice

import dev.robert.network.dto.dto.category.GameGenreResponseDto
import dev.robert.network.dto.dto.games.GamesResponseDto
import dev.robert.network.dto.dto.games.game_details.GameDetailsResponseDto
import dev.robert.network.dto.dto.tags.TagsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GamesApi {
    @GET("genres")
    suspend fun getGameGenres(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("ordering") ordering: String? = null,
    ): GameGenreResponseDto
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("search") search: String? = null,
        @Query("search_exact") searchExact: Boolean? = null,
        @Query("dates") dates: String? = null,
        @Query("ordering") ordering: String? = null,
    ): GamesResponseDto

    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") id: Int
    ) : GameDetailsResponseDto

    @GET("tags")
    suspend fun getTags(): TagsResponseDto
}