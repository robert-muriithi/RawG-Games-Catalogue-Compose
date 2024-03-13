package dev.robert.games.domain.model.game


data class GamesResultModel(
    val added: Int?,
    val backgroundImage: String?,
    val clip: String?,
    val dominantColor: String?,
    val esrbRating: EsrbRatingModel?,
    val genres: List<GenreModel>?,
    val id: Int?,
    val metacritic: Int?,
    val name: String?,
    val parentPlatforms: List<ParentPlatform>?,
    val playtime: Int?,
    val rating: Double?,
    val ratingTop: Int?,
    val ratings: List<Rating>?,
    val ratingsCount: Int?,
    val released: String?,
    val reviewsCount: Int?,
    val reviewsTextCount: Int?,
    val saturatedColor: String?,
    val shortScreenshots: List<ShortScreenshot>?,
    val suggestionsCount: Int?,
    val tags: List<Tag>?,
    val isBookMarked: Boolean = false,
)