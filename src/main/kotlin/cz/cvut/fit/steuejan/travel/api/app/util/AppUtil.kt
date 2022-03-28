@file:Suppress("unused")

package cz.cvut.fit.steuejan.travel.api.app.util

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException


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

suspend inline fun <T> delay(timeMillis: Long, doAfter: () -> T): T {
    kotlinx.coroutines.delay(timeMillis)
    return doAfter.invoke()
}
