package com.lichle.weather.data.local

import com.lichle.weather.data.repository.WeatherRepository
import com.lichle.weather.data.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // Singleton scope for repository
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRealmWeatherRepository(
        realmWeatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

}