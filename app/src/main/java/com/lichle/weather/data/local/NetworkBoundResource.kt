package com.lichle.weather.data.local

import com.lichle.weather.common.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class NetworkBoundResource<ResultType, RequestType> {

    companion object {
        private val TAG = NetworkBoundResource::class.java.simpleName
    }

    fun asFlow(): Flow<ResultType> = flow {
        Logger.d(TAG, "Loading from database...")

        // Step 1: Load from the local database (continuously observe changes)
        val localFlow = loadFromDb()

        // Step 2: Determine if we need to fetch from network (once, based on the first emitted local data)
        val localData = localFlow.firstOrNull()

        if (shouldFetch(localData)) {
            try {
                Logger.d(TAG, "Fetching from network...")

                // Step 3: Fetch data from remote API
                val remoteData = fetchFromNetwork()

                Logger.d(TAG, "Saving network result...")

                // Step 4: Save remote data to local database
                saveNetworkResult(remoteData)
            } catch (e: Exception) {
                // Handle exceptions, but don't wrap in Response. Let higher layers handle them.
                Logger.e(TAG, "Error fetching from network: ${e.message}")
                throw e // Rethrow the error and let higher layers (domain) handle it
            }
        }

        // Step 5: Emit continuously updated data from local database
        emitAll(localFlow)
    }.flowOn(Dispatchers.IO)

    // Abstract methods to be implemented by subclasses
    protected abstract suspend fun fetchFromNetwork(): RequestType
    protected abstract suspend fun saveNetworkResult(item: RequestType)
    protected abstract fun loadFromDb(): Flow<ResultType>
    protected open fun shouldFetch(data: ResultType?): Boolean = true

}