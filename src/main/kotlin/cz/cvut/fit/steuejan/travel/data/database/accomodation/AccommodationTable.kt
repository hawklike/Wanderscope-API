package cz.cvut.fit.steuejan.travel.data.database.accomodation

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.model.AccomodationType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.jodatime.datetime

object AccommodationTable : IntIdTable("accomodation") {
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE)

    val name = varchar("name", DatabaseConfig.NAME_LENGTH)
    val type = enumerationByName("type", AccomodationType.MAX_LENGTH, AccomodationType::class)
    val googlePlaceId = text("google_place_id").nullable()
    val address = text("address").nullable()
    val phone = varchar("phone", DatabaseConfig.PHONE_LENGTH).nullable()
    val email = varchar("email", DatabaseConfig.EMAIL_LENGTH).nullable()
    val website = text("website").nullable()
    val description = text("description").nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
}