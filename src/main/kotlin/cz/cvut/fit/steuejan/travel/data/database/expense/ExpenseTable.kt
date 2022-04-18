package cz.cvut.fit.steuejan.travel.data.database.expense

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.jodatime.datetime

object ExpenseTable : LongIdTable("expenses") {
    val room = reference("room", ExpenseRoomTable, onDelete = CASCADE, onUpdate = CASCADE)
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE)

    val name = varchar("name", DatabaseConfig.NAME_LENGTH)
    val amountInCents = integer("amountInCents")
    val whoPaid = varchar("whoPaid", DatabaseConfig.NAME_LENGTH)
    val whoOwes = text("whoOwes")
    val date = datetime("date")

    fun parseList(list: List<String>): String {
        return list.joinToString(ARRAY_SEPARATOR)
    }

    const val ARRAY_SEPARATOR = ";"
}