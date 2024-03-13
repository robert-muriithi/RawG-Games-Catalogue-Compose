package dev.robert.network.dto.dto.category


import com.google.gson.annotations.SerializedName
import dev.robert.network.dto.dto.category.GenreResponse

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