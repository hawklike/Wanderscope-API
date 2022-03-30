package cz.cvut.fit.steuejan.travel.data.database.trip

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.date

object TripTable : IntIdTable("trips") {
    val name = varchar("name", 140)
    val startDate = date("start_date").nullable()
    val endDate = date("end_date").nullable()
    val description = text("description").nullable()
    val linkView = char("link_view", 8).uniqueIndex().nullable()
    val linkEdit = char("link_edit", 8).uniqueIndex().nullable()
}