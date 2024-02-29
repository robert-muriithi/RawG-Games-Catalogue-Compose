package dev.robert.games.domain.model.game


import com.google.gson.annotations.SerializedName

data class Platform(
    val id: Int,
    val name: String,
    val slug: String
)