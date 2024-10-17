package com.lichle.weather.di

import com.lichle.weather.data.FakeWeatherRepository
import com.lichle.weather.data.local.RepositoryModule
import com.lichle.weather.domain.weather.AddWeatherUseCase
import com.lichle.weather.domain.weather.FetchWeatherUseCase
import com.lichle.weather.domain.weather.GetWeatherUseCase
import com.lichle.weather.domain.weather.SearchWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object RepositoryTestModule {

//    @Provides
//    fun provideTasksRepository(): WeatherRepository {
//        return FakeWeatherRepository()
//    }

//    @Singleton
//    @Provides
//    fun provideAddWeatherUseCase(repos: WeatherRepository): AddWeatherUseCase {
//        return AddWeatherUseCase(repos)
//    }
//
//    @Singleton
//    @Provides
//    fun provideFetchWeatherUseCase(repos: WeatherRepository): FetchWeatherUseCase {
//        return FetchWeatherUseCase(repos)
//    }
//
//    @Singleton
//    @Provides
//    fun provideGetWeatherUseCase(repos: WeatherRepository): GetWeatherUseCase {
//        return GetWeatherUseCase(repos)
//    }
//
//    @Singleton
//    @Provides
//    fun provideSearchWeatherUseCase(repos: WeatherRepository): SearchWeatherUseCase {
//        return SearchWeatherUseCase(repos)
//    }

}