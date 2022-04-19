package cz.cvut.fit.steuejan.travel.data.database.expense.dto

import cz.cvut.fit.steuejan.travel.data.database.expense.ExpenseTable
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class ExpenseDto(
    val id: Long,
    val tripId: Int,
    val roomId: Int,
    val name: String,
    val amountInCents: Long,
    val whoPaid: String,
    val whoOwes: List<String>,
    val date: DateTime?
) : Dto() {
    companion object {
        fun fromDb(resultRow: ResultRow) = ExpenseDto(
            id = resultRow[ExpenseTable.id].value,
            tripId = resultRow[ExpenseTable.trip].value,
            roomId = resultRow[ExpenseTable.room].value,
            name = resultRow[ExpenseTable.name],
            amountInCents = resultRow[ExpenseTable.amountInCents],
            whoPaid = resultRow[ExpenseTable.whoPaid],
            whoOwes = resultRow[ExpenseTable.whoOwes].split(ExpenseTable.ARRAY_SEPARATOR),
            date = resultRow[ExpenseTable.date]
        )
    }
}
