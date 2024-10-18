package com.lichle.weather.data.local.city

import com.lichle.weather.common.Logger
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LocalCityDataSourceImpl @Inject constructor(
    private val _realmConfig: RealmConfiguration
) : LocalCityDataSource {

    override fun getCityFlow(id: Int): Flow<CityObject> {
        return flow {
            val realm = Realm.open(_realmConfig)
            try {
                val cityFlow = realm.query<CityObject>("id == $0", id)
                    .asFlow()
                    .mapNotNull { it.list.firstOrNull() }
                    .map { city ->
                        // Create a detached copy of the city object
                        realm.copyFromRealm(city)
                    }
                emitAll(cityFlow)
            } finally {
                // Close realm when flow collection is cancelled
                realm.close()
            }
        }
    }

    override fun getCityListFlow(): Flow<List<CityObject>> {
        return flow {
            val realm = Realm.open(_realmConfig)
            try {
                val cityListFlow = realm.query<CityObject>()
                    .asFlow()
                    .map { results ->
                        // Create detached copies of all cities
                        results.list.map { city -> realm.copyFromRealm(city) }
                    }
                emitAll(cityListFlow)
            } finally {
                realm.close()
            }
        }
    }

    override suspend fun getCity(id: Int): CityObject? {
        return withRealm { realm ->
            realm.query(CityObject::class, "id == $0", id).first().find()?.let { cityObject ->
                realm.copyFromRealm(cityObject)  // Detach the object from the Realm
            }
        }
    }

    override suspend fun addCity(city: CityObject) {
        withRealm { realm ->
            realm.write {
                copyToRealm(city)  // Now this properly handles the suspend function write
            }
        }
    }

    override suspend fun deleteCity(id: Int) {
        withRealm { realm ->
            realm.write {
                val cityToDelete = query(CityObject::class, "id == $0", id).first().find()
                cityToDelete?.let { delete(it) }
            }
        }
    }

    private suspend fun <T> withRealm(block: suspend (Realm) -> T): T {
        val realm = Realm.open(_realmConfig)
        return try {
            block(realm) 
        } finally {
            realm.close()
        }
    }

}