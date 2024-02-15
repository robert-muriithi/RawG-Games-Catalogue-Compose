package dev.robert.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.robert.database.entities.GameEntity

@ProvidedTypeConverter
class ProductEntityConverters(
    private val gson : Gson
) {

    @TypeConverter
    fun fromProductEntityList(gameEntityList: List<GameEntity>): String {
        val type = object : TypeToken<List<GameEntity>>() {}.type
        return gson.toJson(gameEntityList, type)
    }

    @TypeConverter
    fun toProductEntityList(productEntityListString: String): List<GameEntity> {
        val type = object : TypeToken<List<GameEntity>>() {}.type
        return gson.fromJson(productEntityListString, type)
    }


}