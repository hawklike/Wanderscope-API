package cz.cvut.fit.steuejan.travel.data.extension

import cz.cvut.fit.steuejan.travel.data.util.transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

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

fun <T : Table> T.insertOrNull(body: T.(InsertStatement<Number>) -> Unit): InsertStatement<Number>? {
    return try {
        insert(body)
    } catch (ex: Exception) {
        null
    }
}

fun <Key : Comparable<Key>, T : IdTable<Key>> T.insertAndGetIdOrNull(body: T.(InsertStatement<EntityID<Key>>) -> Unit): EntityID<Key>? {
    return try {
        insertAndGetId(body)
    } catch (ex: Exception) {
        null
    }
}
