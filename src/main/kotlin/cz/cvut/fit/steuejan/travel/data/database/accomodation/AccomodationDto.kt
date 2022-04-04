package cz.cvut.fit.steuejan.travel.data.database.accomodation

import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto
import cz.cvut.fit.steuejan.travel.data.model.AccomodationType
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Contact
import cz.cvut.fit.steuejan.travel.data.model.Duration
import org.jetbrains.exposed.sql.ResultRow

data class AccomodationDto(
    override val id: Int,
    override val tripId: Int,
    override val duration: Duration,
    val name: String,
    val type: AccomodationType,
    val address: Address,
    val contact: Contact,
    val description: String?
) : PointOfInterestDto {
    companion object {
        fun fromDb(resultRow: ResultRow) = AccomodationDto(
            id = resultRow[AccomodationTable.id].value,
            tripId = resultRow[AccomodationTable.trip].value,
            duration = Duration(
                startDate = resultRow[AccomodationTable.startDate],
                endDate = resultRow[AccomodationTable.endDate],
            ),
            name = resultRow[AccomodationTable.name],
            type = resultRow[AccomodationTable.type],
            address = Address(
                googlePlaceId = resultRow[AccomodationTable.googlePlaceId],
                address = resultRow[AccomodationTable.address]
            ),
            contact = Contact(
                phone = resultRow[AccomodationTable.phone],
                email = resultRow[AccomodationTable.email],
                website = resultRow[AccomodationTable.website]
            ),
            description = resultRow[AccomodationTable.description]
        )
    }
}
