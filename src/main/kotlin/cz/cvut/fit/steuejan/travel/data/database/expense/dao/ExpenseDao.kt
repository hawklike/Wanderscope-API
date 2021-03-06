package cz.cvut.fit.steuejan.travel.data.database.expense.dao

import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseDto

interface ExpenseDao {
    suspend fun addExpense(tripId: Int, roomId: Int, expense: ExpenseDto): Long
    suspend fun showExpense(tripId: Int, roomId: Int, expenseId: Long): ExpenseDto?
    suspend fun editExpense(tripId: Int, roomId: Int, expenseId: Long, expense: ExpenseDto): Boolean
    suspend fun deleteExpense(tripId: Int, roomId: Int, expenseId: Long): Boolean
    suspend fun showExpenses(tripId: Int, roomId: Int): List<ExpenseDto>
}