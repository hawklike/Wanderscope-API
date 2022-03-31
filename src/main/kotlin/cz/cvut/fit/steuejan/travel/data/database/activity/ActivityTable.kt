package cz.cvut.fit.steuejan.travel.data.database.activity

import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.model.ActivityType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object ActivityTable : IntIdTable("activities") {
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE)

    val name = varchar("name", 140)
    val type = enumerationByName("type", 20, ActivityType::class).nullable()
    val googlePlaceId = text("google_place_id").nullable()
    val longitude = varchar("longitude", 15).nullable()
    val latitude = varchar("latitude", 15).nullable()
    val mapLink = text("map_link").nullable()
    val description = text("description").nullable()
}