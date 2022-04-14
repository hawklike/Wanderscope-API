package cz.cvut.fit.steuejan.travel.data.database.trip

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.ReferenceOption.SET_NULL
import org.jetbrains.exposed.sql.jodatime.date

object TripTable : IntIdTable("trips") {
    val name = varchar("name", DatabaseConfig.NAME_LENGTH)
    val owner = reference("owner", UserTable, onDelete = SET_NULL, onUpdate = CASCADE)
    val startDate = date("start_date").nullable()
    val endDate = date("end_date").nullable()
    val description = varchar("description", DatabaseConfig.DESCRIPTION_LENGTH).nullable()
    val imageUrl = varchar("image_url", DatabaseConfig.TEXT_MAX_LENGTH).nullable()
    val linkView = char("link_view", DatabaseConfig.TRIP_lINK_LENGTH).uniqueIndex().nullable()
    val linkEdit = char("link_edit", DatabaseConfig.TRIP_lINK_LENGTH).uniqueIndex().nullable()
}