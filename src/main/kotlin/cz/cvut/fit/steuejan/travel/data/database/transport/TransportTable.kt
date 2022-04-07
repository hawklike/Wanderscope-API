package cz.cvut.fit.steuejan.travel.data.database.transport

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.model.TransportType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.jodatime.datetime

object TransportTable : IntIdTable("transports") {
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE)

    val name = varchar("name", DatabaseConfig.NAME_LENGTH)
    val type = enumerationByName("type", 10, TransportType::class)
    val fromGooglePlaceId = text("from_google_place_id").nullable()
    val fromAddress = text("from_address").nullable()
    val toGooglePlaceId = text("to_google_place_id").nullable()
    val toAddress = text("to_address").nullable()
    val description = text("description").nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
    val cars = text("cars").nullable()
    val seats = text("seats").nullable()

    fun parseList(list: List<String>?): String? {
        return list?.joinToString(ARRAY_SEPARATOR)
    }

    const val ARRAY_SEPARATOR = "$"
}