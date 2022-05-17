package cz.cvut.fit.steuejan.travel.data.database.place

import cz.cvut.fit.steuejan.travel.api.trip.poi.place.response.PlaceResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Contact
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.PlaceType
import org.jetbrains.exposed.sql.ResultRow

data class PlaceDto(
    override val id: Int,
    override val tripId: Int,
    override val duration: Duration,
    override val name: String,
    val type: PlaceType,
    val address: Address,
    val contact: Contact,
    val wikiBrief: String?,
    val wikiBriefCzech: String?,
    val imageUrl: String?,
    val description: String?
) : PointOfInterestDto, Dto() {

    companion object {
        fun fromDb(resultRow: ResultRow) = PlaceDto(
            id = resultRow[PlaceTable.id].value,
            tripId = resultRow[PlaceTable.trip].value,
            duration = Duration(
                startDate = resultRow[PlaceTable.startDate],
                endDate = resultRow[PlaceTable.endDate],
            ),
            name = resultRow[PlaceTable.name],
            type = resultRow[PlaceTable.type],
            address = Address(
                googlePlaceId = resultRow[PlaceTable.googlePlaceId],
                address = resultRow[PlaceTable.address]
            ),
            contact = Contact(
                phone = resultRow[PlaceTable.phone],
                email = resultRow[PlaceTable.email],
                website = resultRow[PlaceTable.website]
            ),
            wikiBrief = resultRow[PlaceTable.wikiBrief],
            wikiBriefCzech = resultRow[PlaceTable.wikiBriefCzech],
            imageUrl = resultRow[PlaceTable.imageUrl],
            description = resultRow[PlaceTable.description]
        )
    }

    override fun toResponse(): AbstractPointOfInterestResponse = PlaceResponse(
        id = id,
        tripId = tripId,
        duration = duration,
        name = name,
        type = type,
        address = address,
        contact = contact,
        wikiBrief = wikiBrief,
        wikiBriefCzech = wikiBriefCzech,
        imageUrl = imageUrl,
        description = description
    )
}
