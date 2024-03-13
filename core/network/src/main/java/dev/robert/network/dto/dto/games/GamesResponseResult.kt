package dev.robert.network.dto.dto.games


import com.google.gson.annotations.SerializedName

data class GamesResponseResult(
    @SerializedName("added")
    val added: Int? = null,
    @SerializedName("background_image")
    val backgroundImage: String? = null,
    @SerializedName("clip")
    val clip: String? = null,
    @SerializedName("dominant_color")
    val dominantColor: String? = null,
    @SerializedName("esrb_rating")
    val esrbRating: EsrbRating? = null,
    @SerializedName("genres")
    val genres: List<GenreResponseDto>? = null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("metacritic")
    val metacritic: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("parent_platforms")
    val parentPlatformDtos: List<ParentPlatformDto>? = null,
    @SerializedName("playtime")
    val playtime: Int? = null,
    @SerializedName("rating")
    val rating: Double? = null,
    @SerializedName("rating_top")
    val ratingTop: Int? = null,
    @SerializedName("ratings")
    val ratings: List<Rating>? = null,
    @SerializedName("ratings_count")
    val ratingsCount: Int? = null,
    @SerializedName("released")
    val released: String? = null,
    @SerializedName("reviews_count")
    val reviewsCount: Int? = null,
    @SerializedName("reviews_text_count")
    val reviewsTextCount: Int? = null,
    @SerializedName("saturated_color")
    val saturatedColor: String? = null,
    @SerializedName("short_screenshots")
    val shortScreenshots: List<ShortScreenshot>? = null,
    @SerializedName("suggestions_count")
    val suggestionsCount: Int? = null,
    @SerializedName("tags")
    val tags: List<Tag>? = null,
)