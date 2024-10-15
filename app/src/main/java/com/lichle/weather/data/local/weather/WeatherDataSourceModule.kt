package com.lichle.weather.data.local.weather

import com.lichle.weather.data.remote.weather.RemoteWeatherDataSource
import com.lichle.weather.data.remote.weather.RemoteWeatherDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WeatherDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalWeatherDataSource(
        dataSource: LocalWeatherDataSourceImp
    ): LocalWeatherDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteWeatherDataSource(
        dataSource: RemoteWeatherDataSourceImp
    ): RemoteWeatherDataSource

}