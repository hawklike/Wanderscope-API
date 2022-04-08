package cz.cvut.fit.steuejan.travel.api.trip.poi.place.request

import cz.cvut.fit.steuejan.travel.api.trip.poi.request.PointOfInterestRequest
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Contact
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.PlaceType
import kotlinx.serialization.Serializable

@Serializable
data class PlaceRequest(
    override val name: String,
    override val duration: Duration?,
    val type: PlaceType = PlaceType.OTHER,
    val address: Address?,
    val contact: Contact?,
    val wikiBrief: String?,
    val imageUrl: String?,
    val description: String?
) : PointOfInterestRequest<PlaceDto>() {

    override fun toDto() = PlaceDto(
        id = Dto.UNKNOWN_ID,
        tripId = Dto.UNKNOWN_ID,
        duration = duration ?: Duration(),
        name = name,
        type = type,
        address = address ?: Address(),
        contact = contact ?: Contact(),
        wikiBrief = wikiBrief,
        imageUrl = imageUrl,
        description = description
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, 'type': PlaceType aka [PARKING, FOOD, NATURE, OTHER], default is OTHER."
    }
}
