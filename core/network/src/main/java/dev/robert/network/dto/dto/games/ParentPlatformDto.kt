package dev.robert.network.dto.dto.games


import com.google.gson.annotations.SerializedName

data class ParentPlatformDto(
    @SerializedName("platform")
    val platform: Platform
)