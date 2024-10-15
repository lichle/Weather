package com.lichle.core_common

import android.database.SQLException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

fun mapException(e: Exception): Pair<Int, String> {
    return when (e) {
        is SocketTimeoutException -> Pair(
            ErrorCodes.TIMEOUT,
            "The server is taking too long to respond. Please try again later."
        )

        is UnknownHostException -> Pair(
            ErrorCodes.UNKNOWN_HOST,
            "Couldn't reach the server. Please check your internet connection."
        )

        is SQLException -> {
            when {
                e.message?.contains("constraint violation", ignoreCase = true) == true ->
                    Pair(ErrorCodes.DB_CONSTRAINT_VIOLATION, "A database constraint was violated.")

                e.message?.contains("connection", ignoreCase = true) == true ->
                    Pair(ErrorCodes.DB_CONNECTION_ERROR, "Failed to connect to the database.")

                else ->
                    Pair(
                        ErrorCodes.DB_UNKNOWN_ERROR,
                        "An unknown database error occurred: ${e.message}"
                    )
            }
        }

        is IllegalStateException ->
            Pair(ErrorCodes.DB_ILLEGAL_STATE, "The database is in an illegal state: ${e.message}")

        is IOException -> Pair(
            ErrorCodes.NETWORK_ERROR,
            "Network error occurred. Please check your internet connection."
        )

        else -> Pair(ErrorCodes.UNKNOWN, "An unexpected error occurred: ${e.message}")
    }
}

suspend fun <T> safeCatch(
    block: suspend () -> T,
    onError: (Throwable) -> Unit = {}
): T? {
    return try {
        block()
    } catch (e: CancellationException) {
        throw e // Ensure that the coroutine cancellation is propagated correctly.
    } catch (e: Exception) {
        onError(e) // Handle other exceptions.
        null
    }
}