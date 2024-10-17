package com.lichle.weather.data.local.city

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LocalCityDataSourceImpl @Inject constructor(
    private val _realm: Realm): LocalCityDataSource {

    override fun getCityFlow(id: Int): Flow<CityObject> {
        return _realm.query(CityObject::class, "id == $0", id).asFlow()
            .map { it.list.first() }
    }

    override fun getCityListFlow(): Flow<List<CityObject>> {
        return _realm.query(CityObject::class).asFlow().map { it.list }
    }

    override suspend fun getCity(id: Int): CityObject? {
        return _realm.query(CityObject::class, "id == $0", id).first().find()
    }

    override suspend fun addCity(weather: CityObject) {
        _realm.write { copyToRealm(weather) }
    }

    override suspend fun deleteCity(id: Int) {
        _realm.write {
            val weather = query(CityObject::class, "id == $0", id).first().find()
            weather?.let { delete(it) }
        }
    }

}