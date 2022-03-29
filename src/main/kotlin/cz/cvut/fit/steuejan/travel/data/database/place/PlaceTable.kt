package cz.cvut.fit.steuejan.travel.data.database.place

import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import org.jetbrains.exposed.dao.id.IntIdTable

object PlaceTable : IntIdTable() {
    val trip = reference("trip", TripTable)
    val googlePlaceId = text("google_place_id")
    val description = text("description")
}