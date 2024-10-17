package com.lichle.weather.data.local.city

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

internal class CityObject : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var lon: Double = 0.0
    var lat: Double = 0.0
    var temperature: Double = 0.0
    var feelsLike: Double = 0.0
    var tempMin: Double = 0.0
    var tempMax: Double = 0.0
    var pressure: Int = 0
    var humidity: Int = 0
    var seaLevel: Int = 0
    var groundLevel: Int = 0
    var visibility: Int = 0
    var windSpeed: Double = 0.0
    var windDeg: Int = 0
    var windGust: Double = 0.0
    var cloudiness: Int = 0
    var weather: RealmList<WeatherSummaryObject> = realmListOf()
    var country: String = ""
    var sunrise: Long = 0L
    var sunset: Long = 0L
    var timestamp: Long = 0L
    var timezone: Int = 0
}

internal class WeatherSummaryObject : RealmObject {
    var id: Int = 0
    var main: String = ""
    var description: String = ""
    var icon: String = ""
}