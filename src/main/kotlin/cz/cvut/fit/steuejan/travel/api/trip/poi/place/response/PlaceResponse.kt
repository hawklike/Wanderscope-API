package cz.cvut.fit.steuejan.travel.api.trip.poi.place.response

import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.model.*
import kotlinx.serialization.Serializable

@Serializable
data class PlaceResponse(
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
    val description: String?,
    val coordinates: Coordinates
) : AbstractPointOfInterestResponse()
