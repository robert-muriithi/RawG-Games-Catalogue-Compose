package dev.robert.games.data.mappers

import dev.robert.database.entities.GameEntity
import dev.robert.database.entities.GenreEntity
import dev.robert.network.dto.dto.category.GameGenreResponseDto
import dev.robert.games.data.dto.category.GameResponse
import dev.robert.games.domain.model.game.EsrbRatingModel
import dev.robert.games.domain.model.game.GamesResponseModel
import dev.robert.games.domain.model.game.GamesResultModel
import dev.robert.games.domain.model.game.GenreModel
import dev.robert.games.domain.model.game.ParentPlatform
import dev.robert.games.domain.model.game_details.GameDetailsModel
import dev.robert.network.dto.dto.games.GamesResponseDto
import dev.robert.games.domain.model.genre.Game
import dev.robert.games.domain.model.genre.GameGenre
import dev.robert.games.domain.model.genre.Genre
import dev.robert.network.dto.dto.category.GenreResponse
import dev.robert.network.dto.dto.games.EsrbRating
import dev.robert.network.dto.dto.games.GamesResponseResult
import dev.robert.network.dto.dto.games.GenreResponseDto
import dev.robert.network.dto.dto.games.ParentPlatformDto
import dev.robert.network.dto.dto.games.Platform
import dev.robert.network.dto.dto.games.Rating
import dev.robert.network.dto.dto.games.ShortScreenshot
import dev.robert.network.dto.dto.games.Tag
import dev.robert.network.dto.dto.games.game_details.GameDetailsResponseDto

fun GameDetailsResponseDto.toModel() : GameDetailsModel = GameDetailsModel(
    achievementsCount = achievementsCount,
    added = added,
    backgroundImage = backgroundImage,
    backgroundImageAdditional = backgroundImageAdditional,
    creatorsCount = creatorsCount,
    description = description,
    id = id,
    metacritic = metacritic,
    moviesCount = moviesCount,
    name = name,
    playtime = playtime,
    slug = slug,
    tba = tba,
    updated = updated,
    website = website
)

fun GameGenreResponseDto.toDomain(): GameGenre = GameGenre(
    count = count,
    next = next,
    previous = previous,
    genreResponses = genreResponses.map { it.toGenresDomain() }
)

fun GenreResponse.toGenresDomain() : Genre = Genre(
    id = id,
    name = name,
    gameResponses = gameResponses.map { it.toGame() },
    gamesCount = gamesCount,
    imageBackground = imageBackground,
    slug = slug
)

fun GenreResponse.toEntity() : GenreEntity = GenreEntity(
    id = id,
    name = name,
    gameResponses = gameResponses,
    gamesCount = gamesCount,
    imageBackground = imageBackground,
    slug = slug
)

fun GameResponse.toGame() : Game = Game(
    id = id,
    name = name,
    added = added,
    slug = slug

)

fun GamesResponseDto.toDomain(): GamesResponseModel = GamesResponseModel(
    count = count,
    next = next,
    previous = previous,
    gamesResponseResults = results.map { it.toGameResultModel() }
)

fun GenreEntity.toDomain() : Genre = Genre(
    id = id,
    name = name,
    gamesCount = gamesCount,
    imageBackground = imageBackground,
    slug = slug,
    gameResponses = gameResponses.map { it.toGame() }
)

fun GameEntity.toDomain() : GamesResultModel = GamesResultModel(
    added = added,
    id = id,
    name = name,
    backgroundImage = backgroundImage,
    clip = clip,
    dominantColor = dominantColor,
    esrbRating = esrbRating?.toRationModel(),
    genres = genreResponseDtos?.map { it.toDomainGenre() },
    metacritic = metacritic,
    parentPlatforms = parentPlatformDtos?.map { it.toModel() },
    playtime = playtime,
    rating = rating,
    ratingTop = ratingTop,
    ratings = ratings?.map { it.toRating() },
    ratingsCount = ratingsCount,
    released = released,
    reviewsCount = reviewsCount,
    reviewsTextCount = reviewsTextCount,
    saturatedColor = saturatedColor,
    shortScreenshots = shortScreenshots?.map { it.toShortScreenshot() },
    suggestionsCount = suggestionsCount,
    tags = tags?.map { it.toTag() },
    isBookMarked = isBookMarked
)

fun GamesResponseResult.toGameResultModel() : GamesResultModel = GamesResultModel(
    added = added,
    id = id,
    name = name,
    backgroundImage = backgroundImage,
    clip = clip,
    dominantColor = dominantColor,
    esrbRating = esrbRating?.toRationModel(),
    genres = genres?.map { it.toDomainGenre() },
    metacritic = metacritic,
    parentPlatforms = parentPlatformDtos?.map { it.toModel() },
    playtime = playtime,
    rating = rating,
    ratingTop = ratingTop,
    ratings = ratings?.map { it.toRating() },
    ratingsCount = ratingsCount,
    released = released,
    reviewsCount = reviewsCount,
    reviewsTextCount = reviewsTextCount,
    saturatedColor = saturatedColor,
    shortScreenshots = shortScreenshots?.map { it.toShortScreenshot() },
    suggestionsCount = suggestionsCount,
    tags = tags?.map { it.toTag() },
)


fun GamesResponseResult.toEntity(searchQuery : String?) : GameEntity = GameEntity(
    added = added,
    backgroundImage = backgroundImage,
    clip = clip,
    dominantColor = dominantColor,
    esrbRating = esrbRating,
    genreResponseDtos = genres,
    id = id,
    metacritic = metacritic,
    name = name,
    parentPlatformDtos = parentPlatformDtos,
    playtime = playtime,
    rating = rating,
    ratingTop = ratingTop,
    ratings = ratings,
    ratingsCount = ratingsCount,
    released = released,
    reviewsCount = reviewsCount,
    reviewsTextCount = reviewsTextCount,
    saturatedColor = saturatedColor,
    shortScreenshots = shortScreenshots,
    suggestionsCount = suggestionsCount,
    tags = tags,
    searchQuery = searchQuery
)

fun GenreResponseDto.toDomainGenre() : GenreModel = GenreModel(
    id = id,
    name = name,
    gamesCount = gamesCount,
    imageBackground = imageBackground,
    slug = slug
)
fun EsrbRating.toRationModel() : EsrbRatingModel = EsrbRatingModel(
    id = id,
    name = name,
    slug = slug
)

fun ParentPlatformDto.toModel() : ParentPlatform = ParentPlatform(
   platform = platform.toModel()
)

fun Platform.toModel() : dev.robert.games.domain.model.game.Platform = dev.robert.games.domain.model.game.Platform(
    id = id,
    name = name,
    slug = slug
)

fun Rating.toRating() : dev.robert.games.domain.model.game.Rating = dev.robert.games.domain.model.game.Rating(
    id = id,
    count = count,
    title = title,
    percent = percent
)

fun ShortScreenshot.toShortScreenshot() : dev.robert.games.domain.model.game.ShortScreenshot = dev.robert.games.domain.model.game.ShortScreenshot(
    id = id,
    image = image
)

fun Tag.toTag() : dev.robert.games.domain.model.game.Tag = dev.robert.games.domain.model.game.Tag(
    id = id,
    name = name,
    slug = slug,
    language = language,
    gamesCount = gamesCount,
    imageBackground = imageBackground
)