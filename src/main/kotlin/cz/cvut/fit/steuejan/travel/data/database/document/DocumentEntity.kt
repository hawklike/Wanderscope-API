package cz.cvut.fit.steuejan.travel.data.database.document

import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccomodationEntity
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityEntity
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceEntity
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportEntity
import cz.cvut.fit.steuejan.travel.data.database.trip.TripEntity
import cz.cvut.fit.steuejan.travel.data.database.user.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DocumentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DocumentEntity>(DocumentTable)

    var owner by UserEntity referencedOn DocumentTable.owner
    var trip by TripEntity referencedOn DocumentTable.trip

    var transport by TransportEntity optionalReferencedOn DocumentTable.transport
    var place by PlaceEntity optionalReferencedOn DocumentTable.place
    var accomodation by AccomodationEntity optionalReferencedOn DocumentTable.accomodation
    var activity by ActivityEntity optionalReferencedOn DocumentTable.activity

    var name by DocumentTable.name
    var created by DocumentTable.created
    var extension by DocumentTable.extension
    var key by DocumentTable.key
    var data by DocumentTable.data
}