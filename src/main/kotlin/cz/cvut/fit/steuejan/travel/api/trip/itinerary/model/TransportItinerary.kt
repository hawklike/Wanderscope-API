package cz.cvut.fit.steuejan.travel.api.trip.itinerary.model

import cz.cvut.fit.steuejan.travel.data.database.transport.TransportDto
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.TransportType
import kotlinx.serialization.Serializable

@Serializable
class TransportItinerary(
    val id: Int,
    val name: String,
    val transport: TransportType,
    val from: Address,
    val to: Address,
    override val duration: Duration,
) : CommonItinerary(ItineraryType.TRANSPORT) {
    companion object {
        fun fromDto(dto: TransportDto) = with(dto) {
            TransportItinerary(id, name, type, from, to, duration)
        }
    }
}