package cz.cvut.fit.steuejan.travel.api.trip.points.transport.response

import cz.cvut.fit.steuejan.travel.api.trip.points.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.TransportType
import kotlinx.serialization.Serializable

@Serializable
data class TransportResponse(
    val id: Int,
    val tripId: Int,
    val name: String,
    val type: TransportType,
    val from: Address,
    val to: Address,
    val description: String?,
    val duration: Duration,
    val cars: List<String>?,
    val seats: List<String>?
) : AbstractPointOfInterestResponse()
