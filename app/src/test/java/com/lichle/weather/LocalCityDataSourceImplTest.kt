package com.lichle.weather

import com.lichle.weather.data.local.city.CityObject
import com.lichle.weather.data.local.city.LocalCityDataSourceImpl
import com.lichle.weather.data.local.city.WeatherSummaryObject
import com.lichle.weather.setup.data.getMockCityObject
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocalCityDataSourceImplTest {
    private lateinit var _realm: Realm
    private lateinit var _realmConfig: RealmConfiguration
    private lateinit var _localCityDataSource: LocalCityDataSourceImpl

    private val _testCity = getMockCityObject("Ho Chi Minh")

    @Before
    fun setup() {
        // Create in-memory Realm configuration for testing
        _realmConfig = RealmConfiguration.Builder(schema =
        setOf(CityObject::class, WeatherSummaryObject::class))
            .name("test-database")
            .inMemory() // Use in-memory database for tests
            .build()

        _realm = Realm.open(_realmConfig)
        _localCityDataSource = LocalCityDataSourceImpl(_realmConfig)
    }

    @After
    fun tearDown() {
        _realm.close()
    }

    @Test
    fun `getCityFlow emits city when exists`() = runTest {
        // Given
        _realm.write {
            copyToRealm(_testCity)
        }

        // When
        val result = _localCityDataSource.getCityFlow(_testCity.id)
            .first() // Get first emission

        // Then
        assertEquals(_testCity.id, result.id)
        assertEquals(_testCity.name, result.name)
    }

    @Test
    fun `getCityFlow emits updates to city`() = runTest {
        // Given
        _realm.write {
            copyToRealm(_testCity)
        }

        val cityFlow = _localCityDataSource.getCityFlow(_testCity.id)
        val initialCity = cityFlow.first()
        assertEquals("Ho Chi Minh", initialCity.name)

        // When
        _realm.write {
            val city = query<CityObject>("id == $0", _testCity.id).first().find()
            city?.name = "Updated City"
        }

        // Then
        val updatedCity = cityFlow.first()
        assertEquals("Updated City", updatedCity.name)
    }

    @Test
    fun `getCity returns city when exists`() = runTest {
        // Given
        _realm.write {
            copyToRealm(_testCity)
        }

        // When
        val result = _localCityDataSource.getCity(_testCity.id)

        // Then
        assertEquals(_testCity.id, result?.id)
        assertEquals(_testCity.name, result?.name)
    }

    @Test
    fun `getCity returns null when city does not exist`() = runTest {
        // When
        val result = _localCityDataSource.getCity(999)

        // Then
        assertNull(result)
    }

    @Test
    fun `addCity successfully adds city to database`() = runTest {
        // When
        _localCityDataSource.addCity(_testCity)

        // Then
        val result = _realm.query<CityObject>("id == $0", _testCity.id).first().find()
        assertEquals(_testCity.id, result?.id)
        assertEquals(_testCity.name, result?.name)
    }

    @Test
    fun `deleteCity successfully removes city from database`() = runTest {
        // Given
        _realm.write {
            copyToRealm(_testCity)
        }

        // When
        _localCityDataSource.deleteCity(_testCity.id)

        // Then
        val result = _realm.query<CityObject>("id == $0", _testCity.id).first().find()
        assertNull(result)
    }

    @Test
    fun `deleteCity does nothing when city does not exist`() = runTest {
        // When & Then (should not throw exception)
        _localCityDataSource.deleteCity(999)
    }
}