package dev.robert.network.dto.dto.games


import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("games_count")
    val gamesCount: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_background")
    val imageBackground: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String
)