package cz.cvut.fit.steuejan.travel.api.trip.points.transport.request

import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.TransportType
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class TransportRequest(
    val name: String,
    val type: TransportType,
    val fromGooglePlaceId: String?,
    val fromAddress: String?,
    val toGooglePlaceId: String?,
    val toAddress: String?,
    val description: String?,
    @Serializable(with = DateTimeSerializer::class)
    val startDate: DateTime?,
    @Serializable(with = DateTimeSerializer::class)
    val endDate: DateTime?,
    val cars: List<String>?,
    val seats: List<String>?
) : Request {
    fun toDto() = TransportDto(
        id = Dto.UNKNOWN_ID,
        tripId = Dto.UNKNOWN_ID,
        duration = Duration(startDate, endDate),
        name = name,
        from = Address(fromGooglePlaceId, fromAddress),
        to = Address(toGooglePlaceId, toAddress),
        type = type,
        description = description,
        cars = cars,
        seats = seats
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, type: TransportType aka [WALK, BIKE, CAR, BUS, TRAIN, FERRY, PUBLIC, PLANE]."
    }
}
