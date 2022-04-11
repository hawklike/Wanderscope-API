package cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.response

import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.model.AccomodationType
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Contact
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
data class AccomodationResponse(
    override val id: Int,
    override val tripId: Int,
    override val duration: Duration,
    override val name: String,
    val address: Address,
    val contact: Contact,
    val type: AccomodationType,
    val description: String?
) : AbstractPointOfInterestResponse()
