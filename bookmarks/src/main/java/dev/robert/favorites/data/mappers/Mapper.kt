package dev.robert.favorites.data.mappers

import dev.robert.database.entities.GameEntity
import dev.robert.favorites.domain.model.Game

fun GameEntity.toDomain() : Game {
    return Game(
        id = id,
        name = name,
        released = released,
        backgroundImage = backgroundImage,
        rating = rating,
        ratingTop = ratingTop,
        ratingsCount = ratingsCount,
        metacritic = metacritic,
        playtime = playtime,
        isBookMarked = isBookMarked,
        searchQuery = searchQuery,
        recentSearch = recentSearch
    )
}

fun Game.toEntity() : GameEntity {
    return GameEntity(
        id = id,
        name = name,
        released = released,
        backgroundImage = backgroundImage,
        rating = rating,
        ratingTop = ratingTop,
        ratingsCount = ratingsCount,
        metacritic = metacritic,
        playtime = playtime,
        isBookMarked = isBookMarked,
        searchQuery = searchQuery,
        recentSearch = recentSearch
    )
}

fun List<GameEntity>.toDomainList() : List<Game> {
    return map { it.toDomain() }
}