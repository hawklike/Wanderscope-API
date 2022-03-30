package cz.cvut.fit.steuejan.travel.data.database.place

import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import org.jetbrains.exposed.dao.id.IntIdTable

object PlaceTable : IntIdTable("places") {
    val trip = reference("trip", TripTable)
    val googlePlaceId = text("google_place_id").uniqueIndex()
    val description = text("description")
}