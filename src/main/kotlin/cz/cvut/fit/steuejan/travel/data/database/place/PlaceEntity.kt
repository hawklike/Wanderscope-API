package cz.cvut.fit.steuejan.travel.data.database.place

import cz.cvut.fit.steuejan.travel.data.database.trip.TripEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PlaceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlaceEntity>(PlaceTable)

    var trip by TripEntity referencedOn PlaceTable.trip

    var name by PlaceTable.name
    var type by PlaceTable.type
    var googlePlaceId by PlaceTable.googlePlaceId
    var wikiBrief by PlaceTable.wikiBrief
    var imageUrl by PlaceTable.imageUrl
    var description by PlaceTable.description
    var startDate by PlaceTable.startDate
    var endDate by PlaceTable.endDate
}