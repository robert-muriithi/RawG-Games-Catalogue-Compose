package dev.robert.network.dto.dto.games


import com.google.gson.annotations.SerializedName

data class Platform(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String
)