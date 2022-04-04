@file:Suppress("unused")

package cz.cvut.fit.steuejan.travel.data.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync

suspend inline fun <T> transaction(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline query: suspend () -> T
): T {
    return newSuspendedTransaction(dispatcher) {
        query.invoke()
    }
}

suspend inline fun <T> transactionAsync(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline query: suspend () -> T
): Deferred<T> {
    return suspendedTransactionAsync(dispatcher) {
        query.invoke()
    }
}