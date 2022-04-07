package cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccomodationDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.AccomodationType
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Contact
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
data class AccomodationRequest(
    val name: String,
    val type: AccomodationType,
    val address: Address?,
    val contact: Contact?,
    val description: String?,
    val duration: Duration?
) : Request {
    fun toDto() = AccomodationDto(
        id = Dto.UNKNOWN_ID,
        tripId = Dto.UNKNOWN_ID,
        duration = duration ?: Duration(),
        name = name,
        type = type,
        address = address ?: Address(),
        contact = contact ?: Contact(),
        description = description
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, 'type': AccomodationType aka [ HOTEL, HOSTEL, PENSION, CAMP, OUTDOOR, AIRBNB]."
    }
}
