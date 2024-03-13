package dev.robert.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.robert.database.entities.GameEntity
import dev.robert.games.data.dto.category.GameResponse
import dev.robert.network.dto.dto.games.EsrbRating
import dev.robert.network.dto.dto.games.GenreResponseDto
import dev.robert.network.dto.dto.games.ParentPlatformDto
import dev.robert.network.dto.dto.games.Platform
import dev.robert.network.dto.dto.games.Rating
import dev.robert.network.dto.dto.games.ShortScreenshot
import dev.robert.network.dto.dto.games.Tag

@ProvidedTypeConverter
class GameEntityConverters(
    private val gson: Gson
) {
    @TypeConverter
    fun fromGameResponseList(gameResponseList: List<GameResponse>): String {
        val type = object : TypeToken<List<GameResponse>>() {}.type
        return gson.toJson(gameResponseList, type)
    }

    @TypeConverter
    fun toGameResponseList(gameResponseListString: String): List<GameResponse> {
        val type = object : TypeToken<List<GameResponse>>() {}.type
        return gson.fromJson(gameResponseListString, type)
    }

    @TypeConverter
    fun fromStringList(stringList: List<String>): String {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(stringList, type)
    }

    @TypeConverter
    fun toStringList(stringListString: String): List<String> {
        val objects = gson.fromJson(stringListString, Array<String>::class.java) as Array<String>
        return objects.toList()
    }

    @TypeConverter
    fun fromEsrbRating(esrbRating: EsrbRating): String {
        val type = object : TypeToken<EsrbRating>() {}.type
        return gson.toJson(esrbRating, type)
    }

    @TypeConverter
    fun toEsrbRating(esrbRatingString: String): EsrbRating {
        return gson.fromJson(esrbRatingString, EsrbRating::class.java)
    }

    @TypeConverter
    fun fromGenreList(genreResponseDtoList: List<GenreResponseDto>): String {
        val type = object : TypeToken<List<GenreResponseDto>>() {}.type
        return gson.toJson(genreResponseDtoList, type)
    }

    @TypeConverter
    fun toGenreList(genreListString: String): List<GenreResponseDto> {
        val type = object : TypeToken<List<GenreResponseDto>>() {}.type
        return gson.fromJson(genreListString, type)
    }

    @TypeConverter
    fun fromParentPlatformList(parentPlatformDtoList: List<ParentPlatformDto>): String {
        val type = object : TypeToken<List<ParentPlatformDto>>() {}.type
        return gson.toJson(parentPlatformDtoList, type)
    }

    @TypeConverter
    fun toParentPlatformList(parentPlatformListString: String): List<ParentPlatformDto> {
        val type = object : TypeToken<List<ParentPlatformDto>>() {}.type
        return gson.fromJson(parentPlatformListString, type)
    }

    @TypeConverter
    fun fromPlatform(platform: Platform): String {
        val type = object : TypeToken<Platform>() {}.type
        return gson.toJson(platform, type)
    }

    @TypeConverter
    fun toPlatform(platformString: String): Platform {
        return gson.fromJson(platformString, Platform::class.java)
    }

    @TypeConverter
    fun fromRatingList(ratingList: List<Rating>): String {
        val type = object : TypeToken<List<Rating>>() {}.type
        return gson.toJson(ratingList, type)
    }

    @TypeConverter
    fun toRatingList(ratingListString: String): List<Rating> {
        val type = object : TypeToken<List<Rating>>() {}.type
        return gson.fromJson(ratingListString, type)
    }

    @TypeConverter
    fun fromShortScreenshotsList(shortScreenshotsList: List<ShortScreenshot>): String {
        val type = object : TypeToken<List<ShortScreenshot>>() {}.type
        return gson.toJson(shortScreenshotsList, type)
    }

    @TypeConverter
    fun toShortScreenshotsList(shortScreenshotsListString: String): List<ShortScreenshot> {
        val type = object : TypeToken<List<ShortScreenshot>>() {}.type
        return gson.fromJson(shortScreenshotsListString, type)
    }

    @TypeConverter
    fun fromTagList(tagList: List<Tag>): String {
        val type = object : TypeToken<List<Tag>>() {}.type
        return gson.toJson(tagList, type)
    }

    @TypeConverter
    fun toTagList(tagListString: String): List<Tag> {
        val type = object : TypeToken<List<Tag>>() {}.type
        return gson.fromJson(tagListString, type)
    }

    @TypeConverter
    fun fromGameEntity(gameEntity: GameEntity): String {
        val type = object : TypeToken<GameEntity>() {}.type
        return gson.toJson(gameEntity, type)
    }

    @TypeConverter
    fun toGameEntity(gameEntityString: String): GameEntity {
        return gson.fromJson(gameEntityString, GameEntity::class.java)
    }

}