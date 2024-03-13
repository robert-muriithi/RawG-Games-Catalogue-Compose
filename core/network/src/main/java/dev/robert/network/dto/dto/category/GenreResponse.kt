package dev.robert.network.dto.dto.category


import com.google.gson.annotations.SerializedName
import dev.robert.games.data.dto.category.GameResponse

data class GenreResponse(
    @SerializedName("games")
    val gameResponses: List<GameResponse>,
    @SerializedName("games_count")
    val gamesCount: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_background")
    val imageBackground: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String
)