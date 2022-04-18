package cz.cvut.fit.steuejan.travel.data.database.expense.dao

import cz.cvut.fit.steuejan.travel.data.database.expense.ExpenseTable
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseDto
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

class ExpenseDaoImpl : ExpenseDao {
    override suspend fun addExpense(tripId: Int, roomId: Int, expense: ExpenseDto): Long {
        TODO("Not yet implemented")
    }

    override suspend fun editExpense(tripId: Int, roomId: Int, expenseId: Long, expense: ExpenseDto): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteExpense(tripId: Int, roomId: Int, expenseId: Long): Boolean {
        TODO("Not yet implemented")
    }

    private fun findById(tripId: Int, roomId: Int, expenseId: Long): Op<Boolean> {
        return (ExpenseTable.trip eq tripId) and (ExpenseTable.room eq roomId) and (ExpenseTable.id eq expenseId)
    }
}