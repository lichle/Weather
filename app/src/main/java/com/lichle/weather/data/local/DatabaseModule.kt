package com.lichle.weather.data.local

import com.lichle.weather.data.local.city.CityObject
import com.lichle.weather.data.local.city.WeatherSummaryObject
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideRealmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder(
            schema = setOf(
                CityObject::class,
                WeatherSummaryObject::class
            )
        ).compactOnLaunch()
            .name("weather.db")
            .build()
    }

}