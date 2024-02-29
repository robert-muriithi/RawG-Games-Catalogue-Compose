package dev.robert.games.data.dto.category


import com.google.gson.annotations.SerializedName

data class GameResponse(
    @SerializedName("added")
    val added: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String
)