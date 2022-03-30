package cz.cvut.fit.steuejan.travel.data.database.accomodation

import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.model.AccomodationType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

object AccomodationTable : IntIdTable("accomodation") {
    val trip = reference("trip", TripTable)

    val name = varchar("name", 280)
    val googlePlaceId = text("google_place_id").nullable()
    val contact = text("contact").nullable()
    val website = text("website").nullable()
    val type = enumerationByName("type", 10, AccomodationType::class)
    val description = text("description").nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
}