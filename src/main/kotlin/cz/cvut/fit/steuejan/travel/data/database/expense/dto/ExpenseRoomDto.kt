package cz.cvut.fit.steuejan.travel.data.database.expense.dto

import cz.cvut.fit.steuejan.travel.data.database.expense.ExpenseRoomTable
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import org.jetbrains.exposed.sql.ResultRow

data class ExpenseRoomDto(
    val id: Int,
    val tripId: Int,
    val name: String,
    val currency: String,
    val persons: List<String>
) : Dto() {
    companion object {
        fun fromDb(resultRow: ResultRow) = ExpenseRoomDto(
            id = resultRow[ExpenseRoomTable.id].value,
            tripId = resultRow[ExpenseRoomTable.trip].value,
            name = resultRow[ExpenseRoomTable.name],
            currency = resultRow[ExpenseRoomTable.currency],
            persons = resultRow[ExpenseRoomTable.persons].split(ExpenseRoomTable.ARRAY_SEPARATOR)
        )
    }
}