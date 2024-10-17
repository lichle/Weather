package com.lichle.weather.domain

import com.lichle.core_common.mapException
import com.lichle.weather.common.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseUseCase<in RequestType: Request<*>, out ResponseType> {

    operator fun invoke(request: RequestType): Flow<Response<ResponseType>> = flow {
        try {
            emit(Response.Loading)
            // Execute the use case logic and emit the flow of data
            emitAll(execute(request).map { result ->
                Response.Success(result)
            })
        } catch (e: CancellationException) {
            throw e // Propagate cancellation exceptions
        } catch (e: Exception) {
            // Handle errors and emit error state
            handleError(e)
            val (errorCode, errorMessage) = mapException(e)
            Logger.e(TAG, "Error in use case: ${e.message}")
            emit(Response.Error(errorCode, errorMessage))
        }
    }

    // Abstract function to be implemented by each use case
    protected abstract suspend fun execute(request: RequestType): Flow<ResponseType>

    // Optional method to handle errors
    protected open fun handleError(e: Throwable) {
        Logger.e(TAG, "Error in use case: ${e.message}")
    }

    companion object {
        private val TAG = BaseUseCase::class.java.simpleName
    }

}

