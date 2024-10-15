package com.lichle.weather.data.local.weather

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface WeatherDao {

    @Query("SELECT * FROM weather WHERE id = :id LIMIT 1")
    fun getWeatherFlow(id: Int): Flow<WeatherEntity>

    @Query("SELECT * FROM weather")
    fun getWeatherFlow(): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity): Long

    @Query("SELECT * FROM weather WHERE id = :id LIMIT 1")
    fun getWeather(id: Int): WeatherEntity?

    @Query("DELETE FROM weather WHERE id = :id")
    suspend fun deleteWeather(id: Int): Int

}