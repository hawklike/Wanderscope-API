@file:Suppress("unused")

package cz.cvut.fit.steuejan.travel.data.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import java.sql.BatchUpdateException

suspend inline fun <T> transaction(dispatcher: CoroutineDispatcher = Dispatchers.IO, crossinline query: () -> T): T {
    return newSuspendedTransaction(dispatcher) {
        query.invoke()
    }
}

suspend inline fun <T> transactionAsync(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline query: () -> T
): Deferred<T> {
    return suspendedTransactionAsync(dispatcher) {
        query.invoke()
    }
}

/**
 * Retries transaction on error.
 * @param tries how many times [transaction] should invoke, max 30 tries
 * @return [transaction] result or null, if error still persists
 */
suspend fun <T> retryOnError(tries: Int = 2, transaction: suspend () -> T): T? {
    if (tries > 30) {
        throw IllegalArgumentException("Maximum limit of tries exceeded.")
    }
    repeat(tries) {
        try {
            return transaction.invoke()
        } catch (e: Exception) {
            when ((e as? ExposedSQLException)?.cause) {
                is BatchUpdateException -> {}
                else -> return null
            }
        }
    }
    return null
}