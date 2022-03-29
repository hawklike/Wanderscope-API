package cz.cvut.fit.steuejan.travel.data.database.place

import cz.cvut.fit.steuejan.travel.data.database.trip.TripEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PlaceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlaceEntity>(PlaceTable)

    var trip by TripEntity referencedOn PlaceTable.trip

    var googlePlaceId by PlaceTable.googlePlaceId
    var description by PlaceTable.description
}