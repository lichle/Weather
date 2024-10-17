package com.lichle.weather.data.repository

import com.lichle.weather.common.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException

abstract class NetworkBoundResource<ResultType, RequestType> {

    companion object {
        private val TAG = NetworkBoundResource::class.java.simpleName
    }

    fun asFlow(id: Int): Flow<ResultType> = flow {
        try {
            // Step 1: Fetch data from remote API
            Logger.d(TAG, "Fetching weather with id: $id from network...")
            val remoteData = fetchFromNetwork(id)

            // Step 2: Save the fetched data to the local database if needed
            if (remoteData != null && shouldSave(id, remoteData)) {
                saveWeatherToLocal(id, remoteData)
            }

            // Step 3: Always load data from the local database
            emitLocalData(id)

        } catch (e: Exception) {
            handleFetchError(e, id)
        }
    }

    /**
     * Saves weather data to the local database and handles any exceptions during saving.
     */
    private suspend fun saveWeatherToLocal(id: Int, remoteData: RequestType) {
        try {
            Logger.d(TAG, "Saving weather with id: $id to local database...")
            saveNetworkResult(remoteData)
        } catch (e: Exception) {
            Logger.e(TAG, "Error saving weather: id: $id to local database: ${e.message}")
            throw e // Propagate error to be handled upstream
        }
    }

    /**
     * Loads and emits the local data from the database.
     */
    private suspend fun FlowCollector<ResultType>.emitLocalData(id: Int) {
        Logger.d(TAG, "Loading weather with id: $id from local database...")
        val localFlow = loadFromDb(id)
        emitAll(localFlow)
    }

    /**
     * Handles fetch errors, falling back to local data in case of network issues.
     */
    private suspend fun FlowCollector<ResultType>.handleFetchError(e: Exception, id: Int) {
        if (e is UnknownHostException) {
            Logger.d(TAG, "Network error, falling back to local data for weather with id: $id")
            emitLocalData(id) // Fallback to local data
        } else {
            throw e // If it's not a network error, rethrow the exception
        }
    }

    // Abstract methods to be implemented by subclasses
    protected abstract suspend fun fetchFromNetwork(id: Int): RequestType?
    protected abstract suspend fun saveNetworkResult(item: RequestType)
    protected abstract fun loadFromDb(id: Int): Flow<ResultType>

    // Method to determine whether we should save the network result
    protected open suspend fun shouldSave(id: Int, data: RequestType): Boolean = false
}