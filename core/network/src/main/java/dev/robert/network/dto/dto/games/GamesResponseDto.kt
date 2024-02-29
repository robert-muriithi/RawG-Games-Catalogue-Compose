package dev.robert.network.dto.dto.games


import com.google.gson.annotations.SerializedName

data class GamesResponseDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<GamesResponseResult>,
)