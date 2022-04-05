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
    val type = enumerationByName("type", 20, ActivityType::class).nullable()
    val googlePlaceId = text("google_place_id").nullable()
    val address = text("address").nullable()
    val longitude = varchar("longitude", DatabaseConfig.LON_LAT_LENTGH).nullable()
    val latitude = varchar("latitude", DatabaseConfig.LON_LAT_LENTGH).nullable()
    val mapLink = text("map_link").nullable()
    val description = text("description").nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
}