package dev.robert.search.data.mappers

import dev.robert.database.entities.GameEntity
import dev.robert.network.dto.dto.games.GamesResponseResult
import dev.robert.search.domain.model.Game

fun GameEntity.toGame() = Game(
    id = id,
    name = name,
    backgroundImage = backgroundImage,
    rating = rating,
    released = released,
    isBookMarked = isBookMarked,
    recentSearch = recentSearch
)

fun Game.toEntity() = GameEntity(
    id = id,
    name = name,
    backgroundImage = backgroundImage,
    rating = rating,
    released = released,
    isBookMarked = isBookMarked,
    recentSearch = recentSearch
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

fun GamesResponseResult.toGame() = Game(
    id = id,
    name = name,
    backgroundImage = backgroundImage,
    rating = rating,
    released = released,
    isBookMarked = false,
    recentSearch = false
)