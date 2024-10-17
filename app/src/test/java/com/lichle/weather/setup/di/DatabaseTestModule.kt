package com.lichle.weather.setup.di

import com.lichle.weather.data.local.DatabaseModule
import com.lichle.weather.data.local.city.CityObject
import com.lichle.weather.data.local.city.WeatherSummaryObject
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object DatabaseTestModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                CityObject::class,
                WeatherSummaryObject::class
            )
        ).compactOnLaunch()
            .build()
        return Realm.open(config)
    }

}