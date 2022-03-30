package cz.cvut.fit.steuejan.travel.data.database.activity

import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.model.ActivityType
import org.jetbrains.exposed.dao.id.IntIdTable

object ActivityTable : IntIdTable("activities") {
    val trip = reference("trip", TripTable)

    val name = varchar("name", 140)
    val type = enumerationByName("type", 20, ActivityType::class).nullable()
    val googlePlaceId = text("google_place_id").nullable()
    val longitude = varchar("longitude", 15).nullable()
    val latitude = varchar("latitude", 15).nullable()
    val mapLink = text("map_link").nullable()
    val description = text("description").nullable()
}