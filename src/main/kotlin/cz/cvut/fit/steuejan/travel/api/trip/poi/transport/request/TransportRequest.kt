package cz.cvut.fit.steuejan.travel.api.trip.poi.transport.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.TransportType
import kotlinx.serialization.Serializable

@Serializable
data class TransportRequest(
    val name: String,
    val type: TransportType,
    val from: Address?,
    val to: Address?,
    val description: String?,
    val duration: Duration?,
    val cars: List<String>?,
    val seats: List<String>?
) : Request {
    fun toDto() = TransportDto(
        id = Dto.UNKNOWN_ID,
        tripId = Dto.UNKNOWN_ID,
        duration = duration ?: Duration(),
        name = name,
        from = from ?: Address(),
        to = to ?: Address(),
        type = type,
        description = description,
        cars = cars,
        seats = seats
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, 'type': TransportType aka [WALK, BIKE, CAR, BUS, TRAIN, FERRY, PUBLIC, PLANE]."
    }
}
