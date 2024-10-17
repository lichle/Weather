package com.lichle.weather.data.local

import com.lichle.weather.data.local.weather.WeatherObject
import com.lichle.weather.data.local.weather.WeatherSummaryObject
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
                WeatherObject::class,
                WeatherSummaryObject::class
            )
        ).compactOnLaunch()
            .build()
    }

    @Provides
    fun provideRealm(realmConfig: RealmConfiguration): Realm {
        return Realm.open(realmConfig) // Provide a new instance per injection
    }

}