package dev.robert.games.domain.model.game


import com.google.gson.annotations.SerializedName

data class Rating(
    val count: Int,
    val id: Int,
    val percent: Double,
    val title: String
)