@file:Suppress("unused")

package cz.cvut.fit.steuejan.travel.api.app.util

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import kotlinx.coroutines.delay


inline fun <T> parseBody(func: () -> T, crossinline onError: () -> Nothing): T {
    return runCatching { func.invoke() }.getOrElse { onError.invoke() }
}

inline fun <T> parseBodyOrBadRequest(message: String, func: () -> T): T {
    return parseBody(func) { throw BadRequestException(message) }
}

inline fun <T> execOrThrow(exception: Exception, call: () -> T?): T {
    return call.invoke() ?: throw exception
}

inline fun <T> execOrNotFound(message: String, call: () -> T?): T {
    return execOrThrow(NotFoundException(message), call)
}

fun <T> T?.throwIfMissing(paramName: String): T {
    return this ?: throw BadRequestException(FailureMessages.missingQueryParam(paramName))
}

/**
 * Retries transaction on error.
 * @param tries how many times [call] should invoke, max 30 tries
 * @return [call] result or null, if error still persists
 */
suspend fun <T> retryOnError(tries: Int = 2, call: suspend () -> T): T? {
    if (tries > 30) {
        throw IllegalArgumentException("Maximum limit of tries exceeded.")
    }
    repeat(tries) {
        try {
            return call.invoke()
        } catch (e: Exception) {
            delay(it * 100L)
        }
    }
    return null
}
