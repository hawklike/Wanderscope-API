package cz.cvut.fit.steuejan.travel.data.extension

import cz.cvut.fit.steuejan.travel.data.util.transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

suspend fun IntIdTable.findById(id: Int, coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO): ResultRow? {
    return try {
        transaction(coroutineDispatcher) {
            select { this@findById.id eq id }.first()
        }
    } catch (ex: Exception) {
        null
    }
}

inline fun Table.selectFirst(query: SqlExpressionBuilder.() -> Op<Boolean>): ResultRow? {
    return this.select { SqlExpressionBuilder.query() }.firstOrNull()
}

fun Int.isDeleted() = this > 0
