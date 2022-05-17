package cz.cvut.fit.steuejan.travel.api.trip.poi.place.request

import cz.cvut.fit.steuejan.travel.api.trip.poi.request.PointOfInterestRequest
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.*
import kotlinx.serialization.Serializable

@Serializable
data class PlaceRequest(
    override val name: String,
    override val duration: Duration?,
    val type: PlaceType = PlaceType.OTHER,
    val address: Address?,
    val contact: Contact?,
    val coordinates: Coordinates?,
    val wikiBrief: String?,
    val wikiBriefCzech: String?,
    val imageUrl: String?,
    val description: String?
) : PointOfInterestRequest<PlaceDto>() {

    override fun toDto() = PlaceDto(
        id = Dto.UNKNOWN_ID,
        tripId = Dto.UNKNOWN_ID,
        duration = duration?.validate() ?: Duration(),
        name = name,
        type = type,
        address = address ?: Address(),
        contact = contact ?: Contact(),
        wikiBrief = wikiBrief,
        wikiBriefCzech = wikiBriefCzech,
        imageUrl = imageUrl,
        description = description,
        coordinates = coordinates ?: Coordinates()
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, 'type': PlaceType aka [PARKING, FOOD, NATURE, OTHER], default is OTHER."
    }
}
