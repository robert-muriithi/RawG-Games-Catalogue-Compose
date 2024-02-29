package dev.robert.games.domain.model.genre

import com.google.gson.annotations.SerializedName

data class Game(
    val added: Int,
    val id: Int,
    val name: String,
    val slug: String
)
