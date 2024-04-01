package dev.robert.favorites.domain.model

data class Game(
    val added: Int? = null,
    val backgroundImage: String? = null,
    val clip: String? = null,
    val id: Int,
    val metacritic: Int? = null,
    val name: String? = null,
    val playtime: Int? = null,
    val rating: Double? = null,
    val ratingTop: Int? = null,
    val ratingsCount: Int? = null,
    val released: String? = null,
    val reviewsCount: Int? = null,
    val reviewsTextCount: Int? = null,
    val saturatedColor: String? = null,
    val suggestionsCount: Int? = null,
    val isBookMarked: Boolean = false,
    val searchQuery: String? = null,
    val recentSearch : Boolean = false
)