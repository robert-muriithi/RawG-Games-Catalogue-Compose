package dev.robert.network.dto.dto.tags


import com.google.gson.annotations.SerializedName

data class TagsResponseDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<TagsResultDto>
)