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
    val type = enumerationByName("type", TransportType.MAX_LENGTH, TransportType::class)
    val fromGooglePlaceId = varchar("from_google_place_id", DatabaseConfig.TEXT_LENGTH).nullable()
    val fromAddress = varchar("from_address", DatabaseConfig.TEXT_LENGTH).nullable()
    val toGooglePlaceId = varchar("to_google_place_id", DatabaseConfig.TEXT_LENGTH).nullable()
    val toAddress = varchar("to_address", DatabaseConfig.TEXT_LENGTH).nullable()
    val description = varchar("description", DatabaseConfig.DESCRIPTION_LENGTH).nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
    val cars = varchar("cars", DatabaseConfig.DESCRIPTION_LENGTH).nullable()
    val seats = varchar("seats", DatabaseConfig.DESCRIPTION_LENGTH).nullable()
    val fromLongitude = varchar("fromLongitude", DatabaseConfig.LON_LAT_LENTGH).nullable()
    val fromLatitude = varchar("fromLatitude", DatabaseConfig.LON_LAT_LENTGH).nullable()
    val toLongitude = varchar("toLongitude", DatabaseConfig.LON_LAT_LENTGH).nullable()
    val toLatitude = varchar("toLatitude", DatabaseConfig.LON_LAT_LENTGH).nullable()

    fun parseList(list: List<String>?): String? {
        return list?.joinToString(ARRAY_SEPARATOR)
    }

    const val ARRAY_SEPARATOR = "\\"
}