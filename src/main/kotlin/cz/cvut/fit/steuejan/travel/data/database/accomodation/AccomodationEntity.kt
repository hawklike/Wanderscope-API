package cz.cvut.fit.steuejan.travel.data.database.accomodation

import cz.cvut.fit.steuejan.travel.data.database.document.DocumentEntity
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentTable
import cz.cvut.fit.steuejan.travel.data.database.trip.TripEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AccomodationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AccomodationEntity>(AccomodationTable)

    var trip by TripEntity referencedOn AccomodationTable.trip

    val documents by DocumentEntity optionalReferrersOn DocumentTable.accomodation

    var name by AccomodationTable.name
    var googlePlaceId by AccomodationTable.googlePlaceId
    var contact by AccomodationTable.contact
    var website by AccomodationTable.website
    var type by AccomodationTable.type
    var description by AccomodationTable.description
    var startDate by AccomodationTable.startDate
    var endDate by AccomodationTable.endDate
}