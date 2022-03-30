package cz.cvut.fit.steuejan.travel.data.database.activity

import cz.cvut.fit.steuejan.travel.data.database.trip.TripEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ActivityEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ActivityEntity>(ActivityTable)

    var trip by TripEntity referencedOn ActivityTable.trip

    var name by ActivityTable.name
    var type by ActivityTable.type
    var googlePlaceId by ActivityTable.googlePlaceId
    var longitude by ActivityTable.longitude
    var latitude by ActivityTable.latitude
    var mapLink by ActivityTable.mapLink
    var description by ActivityTable.description
}