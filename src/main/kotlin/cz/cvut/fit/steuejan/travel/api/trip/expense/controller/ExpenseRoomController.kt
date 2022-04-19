package cz.cvut.fit.steuejan.travel.api.trip.expense.controller

import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.CreatedResponse
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.api.trip.expense.bussiness.SimplifyDebts
import cz.cvut.fit.steuejan.travel.api.trip.expense.model.ExpenseOverview
import cz.cvut.fit.steuejan.travel.api.trip.expense.model.SuggestedPayment
import cz.cvut.fit.steuejan.travel.api.trip.expense.response.ExpenseOverviewListResponse
import cz.cvut.fit.steuejan.travel.api.trip.expense.response.ExpenseRoomResponse
import cz.cvut.fit.steuejan.travel.api.trip.expense.response.SuggestedPaymentsResponse
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseDto
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseRoomDto

class ExpenseRoomController(
    daoFactory: DaoFactory,
    private val validator: Validator
) : AbstractTripController(daoFactory) {

    suspend fun createRoom(userId: Int, tripId: Int, room: ExpenseRoomDto): Response {
        val roomId = upsert(userId, tripId, room) {
            daoFactory.expenseRoomDao.createRoom(tripId, room)
        }
        return CreatedResponse.success(roomId)
    }

    suspend fun showRoom(userId: Int, tripId: Int, roomId: Int): Response {
        viewOrThrow(userId, tripId)
        val room = daoFactory.expenseRoomDao.findRoom(tripId, roomId)
            ?: throw NotFoundException(FailureMessages.EXPENSE_ROOM_NOT_FOUND)
        return ExpenseRoomResponse.success(room)
    }

    suspend fun editRoom(userId: Int, tripId: Int, roomId: Int, room: ExpenseRoomDto): Response {
        upsert(userId, tripId, room) {
            if (!daoFactory.expenseRoomDao.editRoom(tripId, roomId, room)) {
                throw NotFoundException(FailureMessages.EXPENSE_ROOM_NOT_FOUND)
            }
        }
        return Success(Status.NO_CONTENT)
    }

    suspend fun deleteRoom(userId: Int, tripId: Int, roomId: Int): Response {
        editOrThrow(userId, tripId)
        if (!daoFactory.expenseRoomDao.deleteRoom(tripId, roomId)) {
            throw NotFoundException(FailureMessages.EXPENSE_ROOM_NOT_FOUND)
        }
        return Success(Status.NO_CONTENT)
    }

    suspend fun showExpenses(userId: Int, tripId: Int, roomId: Int): Response {
        viewOrThrow(userId, tripId)
        val expenses = daoFactory.expenseDao.showExpenses(tripId, roomId)
        return ExpenseOverviewListResponse.success(expenses.map(ExpenseOverview::fromDto))
    }

    suspend fun showSuggestedPayments(userId: Int, tripId: Int, roomId: Int): Response {
        viewOrThrow(userId, tripId)
        val room = daoFactory.expenseRoomDao.findRoom(tripId, roomId)
            ?: throw NotFoundException(FailureMessages.EXPENSE_ROOM_NOT_FOUND)
        val expenses = daoFactory.expenseDao.showExpenses(tripId, roomId)
        val payments = SimplifyDebts(getPersons(expenses)).suggestPayments(getTransactions(expenses))
        return SuggestedPaymentsResponse.success(payments.map(SuggestedPayment::create))
    }

    private fun getPersons(expenses: List<ExpenseDto>): Array<String> {
        val whoPaid = expenses.map { it.whoPaid }
        val whoOwes = expenses.flatMap { it.whoOwes }
        val persons = (whoPaid + whoOwes).toSet()
        return persons.toTypedArray()
    }

    private fun getTransactions(expenses: List<ExpenseDto>): Array<SimplifyDebts.Transaction> {
        val transactions = mutableListOf<SimplifyDebts.Transaction>()
        expenses.forEach { expense ->
            expense.whoOwes.forEach { whoOwes ->
                transactions.add(
                    SimplifyDebts.Transaction(
                        expense.whoPaid,
                        whoOwes,
                        expense.amountInCents
                    )
                )
            }
        }
        return transactions.toTypedArray()
    }

    private suspend fun <T> upsert(userId: Int, tripId: Int, room: ExpenseRoomDto, dbCall: suspend () -> T): T {
        editOrThrow(userId, tripId)

        if (room.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }

        if (room.currency.length > DatabaseConfig.CURRENCY_CODE_LENGTH) {
            throw BadRequestException(FailureMessages.CURRENCY_TOO_lONG)
        }

        if (!arePersonsUnique(room.persons)) {
            throw BadRequestException(FailureMessages.EXPENSE_PERSONS_NOT_UNIQUE)
        }

        validateNames(room.persons)
        return dbCall.invoke()
    }

    private fun arePersonsUnique(persons: List<String>): Boolean {
        return persons.toSet().size == persons.size
    }

    private suspend fun validateNames(persons: List<String>) {
        persons.forEach {
            validator.validateName(it, "person's name", minLength = 1)
        }
    }
}