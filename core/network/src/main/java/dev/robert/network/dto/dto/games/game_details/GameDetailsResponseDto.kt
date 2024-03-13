package dev.robert.network.dto.dto.games.game_details


import com.google.gson.annotations.SerializedName

data class GameDetailsResponseDto(
    @SerializedName("achievements_count")
    val achievementsCount: Int?,
    @SerializedName("added")
    val added: Int?,
    @SerializedName("background_image")
    val backgroundImage: String?,
    @SerializedName("background_image_additional")
    val backgroundImageAdditional: String?,
    @SerializedName("creators_count")
    val creatorsCount: Int?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("metacritic")
    val metacritic: Int?,
    @SerializedName("movies_count")
    val moviesCount: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("playtime")
    val playtime: Int?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("tba")
    val tba: Boolean?,
    @SerializedName("updated")
    val updated: String?,
    @SerializedName("website")
    val website: String?,
)