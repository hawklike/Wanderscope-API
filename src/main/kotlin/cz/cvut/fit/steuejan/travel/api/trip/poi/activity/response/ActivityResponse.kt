package cz.cvut.fit.steuejan.travel.api.trip.poi.activity.response

import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.model.ActivityType
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Coordinates
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponse(
    override val id: Int,
    override val tripId: Int,
    override val duration: Duration,
    override val name: String,
    val type: ActivityType,
    val address: Address,
    val mapLink: String?,
    val description: String?,
    val website: String?,
    val coordinates: Coordinates,
) : AbstractPointOfInterestResponse()
