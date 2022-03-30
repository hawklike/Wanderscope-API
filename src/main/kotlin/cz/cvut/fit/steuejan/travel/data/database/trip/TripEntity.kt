package cz.cvut.fit.steuejan.travel.data.database.trip

import cz.cvut.fit.steuejan.travel.data.database.place.PlaceEntity
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TripEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TripEntity>(TripTable)

    val places by PlaceEntity referrersOn PlaceTable.trip

    var name by TripTable.name
    var startDate by TripTable.startDate
    var endDate by TripTable.endDate
    var description by TripTable.describtion
    var linkView by TripTable.linkView
    var linkEdit by TripTable.linkEdit
}