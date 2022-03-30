package cz.cvut.fit.steuejan.travel.data.database.transport

import cz.cvut.fit.steuejan.travel.data.database.document.DocumentEntity
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentTable
import cz.cvut.fit.steuejan.travel.data.database.trip.TripEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TransportEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TransportEntity>(TransportTable) {
        const val SEPARATOR = "$"
    }

    var trip by TripEntity referencedOn TransportTable.trip

    val documents by DocumentEntity optionalReferrersOn DocumentTable.transport

    var from by TransportTable.from
    var to by TransportTable.to
    var type by TransportTable.type
    var description by TransportTable.description
    var startDate by TransportTable.startDate
    var endDate by TransportTable.endDate

    //cars is a list because of transform function
    var cars by TransportTable.cars.transform(
        { a -> a?.joinToString(SEPARATOR) },
        { str -> str?.split(SEPARATOR)?.toList() }
    )

    //seats is a list because of transform function
    var seats by TransportTable.seats.transform(
        { a -> a?.joinToString(SEPARATOR) },
        { str -> str?.split(SEPARATOR)?.toList() }
    )
}