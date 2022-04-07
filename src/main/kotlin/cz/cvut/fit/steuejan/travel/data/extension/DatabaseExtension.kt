package cz.cvut.fit.steuejan.travel.data.extension

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

fun IntIdTable.findById(id: Int): ResultRow? {
    return select { this@findById.id eq id }.firstOrNull()
}

inline fun Table.selectFirst(query: SqlExpressionBuilder.() -> Op<Boolean>): ResultRow? {
    return this.select { SqlExpressionBuilder.query() }.firstOrNull()
}

fun Int.isDeleted() = this > 0
fun Int.isUpdated() = this > 0

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

fun <T : IntIdTable> T.updateById(id: Int, limit: Int? = null, body: T.(UpdateStatement) -> Unit): Int {
    return this.update({ this@updateById.id eq id }, limit, body)
}

fun <T : IntIdTable> T.updateByIdOrNull(id: Int, limit: Int? = null, body: T.(UpdateStatement) -> Unit): Int? {
    return try {
        updateById(id, limit, body)
    } catch (ex: Exception) {
        null
    }
}

fun IntIdTable.deleteById(id: Int, limit: Int? = null, offset: Long? = null): Int {
    return deleteWhere(limit, offset) { this@deleteById.id eq id }
}
