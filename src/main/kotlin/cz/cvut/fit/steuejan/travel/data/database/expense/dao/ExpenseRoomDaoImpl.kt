package cz.cvut.fit.steuejan.travel.data.database.expense.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.expense.ExpenseRoomTable
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseRoomDto
import cz.cvut.fit.steuejan.travel.data.extension.insertAndGetIdOrNull
import cz.cvut.fit.steuejan.travel.data.extension.isDeleted
import cz.cvut.fit.steuejan.travel.data.extension.isUpdated
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ExpenseRoomDaoImpl : ExpenseRoomDao {
    override suspend fun createRoom(tripId: Int, room: ExpenseRoomDto) = transaction {
        ExpenseRoomTable.insertAndGetIdOrNull {
            it[trip] = tripId
            it[name] = room.name
            it[currency] = room.currency
            it[persons] = parseList(room.persons)
        }?.value ?: throw BadRequestException(FailureMessages.ADD_EXPENSE_ROOM_FAILURE)
    }

    override suspend fun findRoom(tripId: Int, roomId: Int) = transaction {
        ExpenseRoomTable.selectFirst { findById(tripId, roomId) }
    }?.let(ExpenseRoomDto::fromDb)

    override suspend fun deleteRoom(tripId: Int, roomId: Int) = transaction {
        ExpenseRoomTable.deleteWhere { findById(tripId, roomId) }
    }.isDeleted()

    override suspend fun editRoom(tripId: Int, roomId: Int, room: ExpenseRoomDto) = transaction {
        ExpenseRoomTable.update({ findById(tripId, roomId) }) {
            it[name] = room.name
            it[currency] = room.currency
            it[persons] = parseList(room.persons)
        }
    }.isUpdated()

    override suspend fun editName(tripId: Int, roomId: Int, name: String) = transaction {
        ExpenseRoomTable.update({ findById(tripId, roomId) }) {
            it[this.name] = name
        }
    }.isUpdated()

    override suspend fun editPersons(tripId: Int, roomId: Int, persons: List<String>) = transaction {
        ExpenseRoomTable.update({ findById(tripId, roomId) }) {
            it[this.persons] = parseList(persons)
        }
    }.isUpdated()

    override suspend fun showExpenseRooms(tripId: Int) = transaction {
        ExpenseRoomTable.select { ExpenseRoomTable.trip eq tripId }.map(ExpenseRoomDto::fromDb)
    }

    override suspend fun getPersons(tripId: Int, roomId: Int) = transaction {
        ExpenseRoomTable.selectFirst { findById(tripId, roomId) }
            ?: throw NotFoundException(FailureMessages.EXPENSE_ROOM_NOT_FOUND)
    }.let { it[ExpenseRoomTable.persons].split(ExpenseRoomTable.ARRAY_SEPARATOR) }

    private fun findById(tripId: Int, roomId: Int): Op<Boolean> {
        return (ExpenseRoomTable.trip eq tripId) and (ExpenseRoomTable.id eq roomId)
    }
}