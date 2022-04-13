package cz.cvut.fit.steuejan.travel.data.database.accomodation

import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.response.AccommodationResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto
import cz.cvut.fit.steuejan.travel.data.model.AccomodationType
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Contact
import cz.cvut.fit.steuejan.travel.data.model.Duration
import org.jetbrains.exposed.sql.ResultRow

data class AccommodationDto(
    override val id: Int,
    override val tripId: Int,
    override val duration: Duration,
    override val name: String,
    val type: AccomodationType,
    val address: Address,
    val contact: Contact,
    val description: String?
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
            description = resultRow[AccommodationTable.description]
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
        description = description
    )
}