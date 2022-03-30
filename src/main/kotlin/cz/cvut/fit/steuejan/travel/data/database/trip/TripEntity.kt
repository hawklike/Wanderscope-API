package cz.cvut.fit.steuejan.travel.data.database.trip

import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccomodationEntity
import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccomodationTable
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityEntity
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityTable
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceEntity
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceTable
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportEntity
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TripEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TripEntity>(TripTable)

    val places by PlaceEntity referrersOn PlaceTable.trip
    val transports by TransportEntity referrersOn TransportTable.trip
    val accomodation by AccomodationEntity referrersOn AccomodationTable.trip
    val activities by ActivityEntity referrersOn ActivityTable.trip

    var name by TripTable.name
    var startDate by TripTable.startDate
    var endDate by TripTable.endDate
    var description by TripTable.description
    var linkView by TripTable.linkView
    var linkEdit by TripTable.linkEdit
}