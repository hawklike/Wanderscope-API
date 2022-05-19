package cz.cvut.fit.steuejan.travel.api.trip.poi.transport.response

import cz.cvut.fit.steuejan.travel.api.trip.poi.response.AbstractPointOfInterestResponse
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Coordinates
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.TransportType
import kotlinx.serialization.Serializable

@Serializable
data class TransportResponse(
    override val id: Int,
    override val tripId: Int,
    override val name: String,
    override val duration: Duration,
    val type: TransportType,
    val from: Address,
    val to: Address,
    val description: String?,
    val cars: List<String>?,
    val seats: List<String>?,
    val fromCoordinates: Coordinates,
    val toCoordinates: Coordinates
) : AbstractPointOfInterestResponse()
