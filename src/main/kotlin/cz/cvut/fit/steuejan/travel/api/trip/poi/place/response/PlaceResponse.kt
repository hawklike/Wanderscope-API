package cz.cvut.fit.steuejan.travel.api.trip.poi.place.response

import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Contact
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.PlaceType
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
    val imageUrl: String?,
    val description: String?
) : AbstractPointOfInterestResponse()
