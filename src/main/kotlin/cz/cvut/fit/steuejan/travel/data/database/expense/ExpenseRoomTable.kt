package cz.cvut.fit.steuejan.travel.data.database.expense

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object ExpenseRoomTable : IntIdTable("expense_rooms") {
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE).uniqueIndex()

    val name = varchar("name", DatabaseConfig.NAME_LENGTH)
    val currency = varchar("currency", DatabaseConfig.CURRENCY_CODE_LENGTH)
    val persons = text("persons")

    fun parseList(list: List<String>): String {
        return list.joinToString(ARRAY_SEPARATOR)
    }

    const val ARRAY_SEPARATOR = ";"
}