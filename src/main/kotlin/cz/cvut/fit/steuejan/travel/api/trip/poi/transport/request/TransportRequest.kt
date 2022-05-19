package cz.cvut.fit.steuejan.travel.api.trip.poi.transport.request

import cz.cvut.fit.steuejan.travel.api.trip.poi.request.PointOfInterestRequest
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Coordinates
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.TransportType
import kotlinx.serialization.Serializable

@Serializable
data class TransportRequest(
    override val name: String,
    override val duration: Duration?,
    val type: TransportType,
    val from: Address?,
    val to: Address?,
    val description: String?,
    val cars: List<String>?,
    val seats: List<String>?,
    val fromCoordinates: Coordinates?,
    val toCoordinates: Coordinates?
) : PointOfInterestRequest<TransportDto>() {

    override fun toDto() = TransportDto(
        id = Dto.UNKNOWN_ID,
        tripId = Dto.UNKNOWN_ID,
        duration = duration?.validate() ?: Duration(),
        name = name,
        from = from ?: Address(),
        to = to ?: Address(),
        type = type,
        description = description,
        cars = cars,
        seats = seats,
        fromCoordinates = fromCoordinates ?: Coordinates(),
        toCoordinates = toCoordinates ?: Coordinates()
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, 'type': TransportType aka [WALK, BIKE, CAR, BUS, TRAIN, FERRY, PUBLIC, PLANE, OTHER]."
    }
}
