package com.lichle.weather.data.local.city

import com.lichle.weather.data.remote.city.RemoteCityDataSource
import com.lichle.weather.data.remote.city.RemoteCityDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CityDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRealmLocalWeatherDataSource(
        dataSource: LocalCityDataSourceImpl
    ): LocalCityDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteWeatherDataSource(
        dataSource: RemoteCityDataSourceImp
    ): RemoteCityDataSource

}