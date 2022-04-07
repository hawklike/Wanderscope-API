package cz.cvut.fit.steuejan.travel.data.database.transport

import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.response.TransportResponse
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.TransportType
import org.jetbrains.exposed.sql.ResultRow

data class TransportDto(
    override val id: Int,
    override val tripId: Int,
    override val duration: Duration,
    override val name: String,
    val from: Address,
    val to: Address,
    val type: TransportType,
    val description: String?,
    val cars: List<String>?,
    val seats: List<String>?
) : PointOfInterestDto, Dto() {
    companion object {
        fun fromDb(resultRow: ResultRow) = TransportDto(
            id = resultRow[TransportTable.id].value,
            name = resultRow[TransportTable.name],
            tripId = resultRow[TransportTable.trip].value,
            duration = Duration(
                startDate = resultRow[TransportTable.startDate],
                endDate = resultRow[TransportTable.endDate],
            ),
            from = Address(
                googlePlaceId = resultRow[TransportTable.fromGooglePlaceId],
                address = resultRow[TransportTable.fromAddress]
            ),
            to = Address(
                googlePlaceId = resultRow[TransportTable.toGooglePlaceId],
                address = resultRow[TransportTable.toAddress]
            ),
            type = resultRow[TransportTable.type],
            description = resultRow[TransportTable.description],
            cars = resultRow[TransportTable.cars]?.split(TransportTable.ARRAY_SEPARATOR),
            seats = resultRow[TransportTable.cars]?.split(TransportTable.ARRAY_SEPARATOR),
        )
    }

    override fun toResponse(): AbstractPointOfInterestResponse = TransportResponse(
        id = id,
        tripId = tripId,
        name = name,
        duration = duration,
        type = type,
        from = from,
        to = to,
        description = description,
        cars = cars,
        seats = seats
    )
}
