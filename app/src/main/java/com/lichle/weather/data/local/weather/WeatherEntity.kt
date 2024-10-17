package com.lichle.weather.data.local.weather

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "weather")
@TypeConverters(WeatherSummaryConverters::class)
data class WeatherEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val lon: Double,
    val lat: Double,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val seaLevel: Int?,
    val groundLevel: Int?,
    val visibility: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double?,
    val cloudiness: Int,
    val weather: List<WeatherSummaryEntity>, // Store the list of Weather objects
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long,
    val timezone: Int
)

data class WeatherSummaryEntity(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

internal class WeatherSummaryConverters {

    @TypeConverter
    fun fromWeatherList(value: List<WeatherSummaryEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toWeatherList(value: String): List<WeatherSummaryEntity> {
        val listType = object : TypeToken<List<WeatherSummaryEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }
}