package cz.cvut.fit.steuejan.travel.data.database.expense.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.expense.ExpenseTable
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseDto
import cz.cvut.fit.steuejan.travel.data.extension.insertAndGetIdOrNull
import cz.cvut.fit.steuejan.travel.data.extension.isDeleted
import cz.cvut.fit.steuejan.travel.data.extension.isUpdated
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

class ExpenseDaoImpl : ExpenseDao {
    override suspend fun addExpense(tripId: Int, roomId: Int, expense: ExpenseDto) = transaction {
        ExpenseTable.insertAndGetIdOrNull {
            it[room] = roomId
            it[trip] = tripId
            it[name] = expense.name
            it[amountInCents] = expense.amountInCents
            it[whoPaid] = expense.whoPaid
            it[whoOwes] = parseList(expense.whoOwes)
            it[date] = expense.date
        }?.value ?: throw BadRequestException(FailureMessages.ADD_EXPENSE_FAILURE)
    }

    override suspend fun showExpense(tripId: Int, roomId: Int, expenseId: Long) = transaction {
        ExpenseTable.selectFirst { findById(tripId, roomId, expenseId) }
    }?.let(ExpenseDto::fromDb)

    override suspend fun editExpense(tripId: Int, roomId: Int, expenseId: Long, expense: ExpenseDto) = transaction {
        ExpenseTable.update({ findById(tripId, roomId, expenseId) }) {
            it[name] = expense.name
            it[amountInCents] = expense.amountInCents
            it[whoPaid] = expense.whoPaid
            it[whoOwes] = parseList(expense.whoOwes)
            it[date] = expense.date
        }
    }.isUpdated()

    override suspend fun deleteExpense(tripId: Int, roomId: Int, expenseId: Long) = transaction {
        ExpenseTable.deleteWhere { findById(tripId, roomId, expenseId) }
    }.isDeleted()

    private fun findById(tripId: Int, roomId: Int, expenseId: Long): Op<Boolean> {
        return (ExpenseTable.trip eq tripId) and (ExpenseTable.room eq roomId) and (ExpenseTable.id eq expenseId)
    }
}