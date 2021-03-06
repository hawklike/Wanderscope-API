package cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.response

import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.model.*
import kotlinx.serialization.Serializable

@Serializable
data class AccommodationResponse(
    override val id: Int,
    override val tripId: Int,
    override val duration: Duration,
    override val name: String,
    val address: Address,
    val contact: Contact,
    val type: AccommodationType,
    val description: String?,
    val coordinates: Coordinates
) : AbstractPointOfInterestResponse()
