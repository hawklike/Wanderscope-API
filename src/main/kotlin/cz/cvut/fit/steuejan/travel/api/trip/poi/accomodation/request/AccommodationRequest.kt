package cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.request

import cz.cvut.fit.steuejan.travel.api.trip.poi.request.PointOfInterestRequest
import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccommodationDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.*
import kotlinx.serialization.Serializable

@Serializable
data class AccommodationRequest(
    override val name: String,
    override val duration: Duration?,
    val type: AccommodationType,
    val address: Address?,
    val contact: Contact?,
    val description: String?,
    val coordinates: Coordinates?
) : PointOfInterestRequest<AccommodationDto>() {

    override fun toDto() = AccommodationDto(
        id = Dto.UNKNOWN_ID,
        tripId = Dto.UNKNOWN_ID,
        duration = duration?.validate() ?: Duration(),
        name = name,
        type = type,
        address = address ?: Address(),
        contact = contact ?: Contact(),
        description = description,
        coordinates = coordinates ?: Coordinates()
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, 'type': AccommodationType aka [HOTEL, HOSTEL, PENSION, CAMP, OUTDOOR, AIRBNB, OTHER]."
    }
}
