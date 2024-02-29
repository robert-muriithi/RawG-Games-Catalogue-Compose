package dev.robert.games.data.dto.category


import com.google.gson.annotations.SerializedName

data class GameGenreResponseDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val genreResponses: List<GenreResponse>
)