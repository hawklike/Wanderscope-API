package cz.cvut.fit.steuejan.travel.data.database.trip

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.date

object TripTable : IntIdTable("trips") {
    val name = varchar("name", DatabaseConfig.NAME_LENGTH)
    val startDate = date("start_date").nullable()
    val endDate = date("end_date").nullable()
    val description = text("description").nullable()
    val imageUrl = text("image_url").nullable()
    val linkView = char("link_view", DatabaseConfig.TRIP_lINK_LENGTH).uniqueIndex().nullable()
    val linkEdit = char("link_edit", DatabaseConfig.TRIP_lINK_LENGTH).uniqueIndex().nullable()
}