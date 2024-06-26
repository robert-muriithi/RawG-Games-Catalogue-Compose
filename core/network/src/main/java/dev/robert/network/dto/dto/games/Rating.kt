package dev.robert.network.dto.dto.games


import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("count")
    val count: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("percent")
    val percent: Double,
    @SerializedName("title")
    val title: String
)