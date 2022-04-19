package cz.cvut.fit.steuejan.travel.api.trip.expense.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.CreatedResponse
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.api.trip.expense.response.ExpenseResponse
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.expense.ExpenseTable.room
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseDto

class ExpenseController(daoFactory: DaoFactory) : AbstractTripController(daoFactory) {

    suspend fun addExpense(userId: Int, tripId: Int, roomId: Int, expense: ExpenseDto): Response {
        val id = upsert(userId, tripId, roomId, expense) {
            daoFactory.expenseDao.addExpense(tripId, roomId, expense)
        }
        return CreatedResponse.success(id)
    }

    suspend fun showExpense(userId: Int, tripId: Int, roomId: Int, expenseId: Long): Response {
        viewOrThrow(userId, tripId)
        val expense = daoFactory.expenseDao.showExpense(tripId, roomId, expenseId)
            ?: throw NotFoundException(FailureMessages.EXPENSE_NOT_FOUND)
        return ExpenseResponse.success(expense)
    }

    suspend fun editExpense(userId: Int, tripId: Int, roomId: Int, expenseId: Long, expense: ExpenseDto): Response {
        upsert(userId, tripId, roomId, expense) {
            if (!daoFactory.expenseDao.editExpense(tripId, roomId, expenseId, expense)) {
                throw NotFoundException(FailureMessages.EXPENSE_NOT_FOUND)
            }
        }
        return Success(Status.NO_CONTENT)
    }

    suspend fun deleteExpense(userId: Int, tripId: Int, roomId: Int, expenseId: Long): Response {
        editOrThrow(userId, tripId)
        if (!daoFactory.expenseDao.deleteExpense(tripId, roomId, expenseId)) {
            throw NotFoundException(FailureMessages.EXPENSE_NOT_FOUND)
        }
        return Success(Status.NO_CONTENT)
    }

    private suspend fun <T> upsert(
        userId: Int,
        tripId: Int,
        roomId: Int,
        expense: ExpenseDto,
        dbCall: suspend () -> T
    ): T {
        editOrThrow(userId, tripId)

        if (room.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }

        val room = daoFactory.expenseRoomDao.findRoom(tripId, roomId)
            ?: throw NotFoundException(FailureMessages.EXPENSE_ROOM_NOT_FOUND)

        validatePersons(room.persons, expense.whoPaid, expense.whoOwes)
        return dbCall.invoke()
    }

    private fun validatePersons(persons: List<String>, whoPaid: String, whoOwes: List<String>) {
        if (!persons.contains(whoPaid)) {
            throw BadRequestException(FailureMessages.expensePersonNotFound(whoPaid))
        }

        val notFound = whoOwes.fold(emptyList<String>()) { acc, person ->
            if (!persons.contains(person)) acc + person else acc
        }

        if (notFound.isNotEmpty()) {
            throw BadRequestException(FailureMessages.expensePersonNotFound(*notFound.toTypedArray()))
        }
    }
}