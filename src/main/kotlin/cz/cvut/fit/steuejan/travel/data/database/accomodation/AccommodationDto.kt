package cz.cvut.fit.steuejan.travel.data.database.accomodation

import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.response.AccommodationResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto
import cz.cvut.fit.steuejan.travel.data.model.*
import org.jetbrains.exposed.sql.ResultRow

data class AccommodationDto(
    override val id: Int,
    override val tripId: Int,
    override val duration: Duration,
    override val name: String,
    val type: AccommodationType,
    val address: Address,
    val contact: Contact,
    val description: String?,
    val coordinates: Coordinates
) : PointOfInterestDto, Dto() {

    companion object {
        fun fromDb(resultRow: ResultRow) = AccommodationDto(
            id = resultRow[AccommodationTable.id].value,
            tripId = resultRow[AccommodationTable.trip].value,
            duration = Duration(
                startDate = resultRow[AccommodationTable.startDate],
                endDate = resultRow[AccommodationTable.endDate],
            ),
            name = resultRow[AccommodationTable.name],
            type = resultRow[AccommodationTable.type],
            address = Address(
                googlePlaceId = resultRow[AccommodationTable.googlePlaceId],
                address = resultRow[AccommodationTable.address]
            ),
            contact = Contact(
                phone = resultRow[AccommodationTable.phone],
                email = resultRow[AccommodationTable.email],
                website = resultRow[AccommodationTable.website]
            ),
            description = resultRow[AccommodationTable.description],
            coordinates = Coordinates(
                longitude = resultRow[AccommodationTable.longitude],
                latitude = resultRow[AccommodationTable.latitude]
            )
        )
    }

    override fun toResponse(): AbstractPointOfInterestResponse = AccommodationResponse(
        id = id,
        tripId = tripId,
        duration = duration,
        name = name,
        address = address,
        contact = contact,
        type = type,
        description = description,
        coordinates = coordinates
    )
}
