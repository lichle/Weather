package com.lichle.weather.di

import android.content.Context
import androidx.room.Room
import com.lichle.weather.data.local.AppDatabase
import com.lichle.weather.data.local.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object DatabaseTestModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}