package com.lichle.weather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lichle.weather.data.local.weather.WeatherDao
import com.lichle.weather.data.local.weather.WeatherEntity

// Add the list of entity classes and set the database version
@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        // Build the Room database instance
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app-database")
                .build()
    }
}