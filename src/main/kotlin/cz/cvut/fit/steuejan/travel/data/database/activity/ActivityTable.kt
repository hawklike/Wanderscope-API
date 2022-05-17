package cz.cvut.fit.steuejan.travel.data.database.activity

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.model.ActivityType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.jodatime.datetime

object ActivityTable : IntIdTable("activities") {
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE)

    val name = varchar("name", DatabaseConfig.NAME_LENGTH)
    val type = enumerationByName("type", ActivityType.MAX_LENGTH, ActivityType::class).nullable()
    val googlePlaceId = varchar("google_place_id", DatabaseConfig.TEXT_LENGTH).nullable()
    val address = varchar("address", DatabaseConfig.TEXT_LENGTH).nullable()
    val mapLink = varchar("map_link", DatabaseConfig.TEXT_LENGTH).nullable()
    val description = varchar("description", DatabaseConfig.DESCRIPTION_LENGTH).nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
}