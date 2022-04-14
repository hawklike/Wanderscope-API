package cz.cvut.fit.steuejan.travel.api.trip.itinerary.model

import cz.cvut.fit.steuejan.travel.data.database.place.PlaceDto
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.PlaceType
import kotlinx.serialization.Serializable

@Serializable
class PlaceItinerary(
    val id: Int,
    val name: String,
    val place: PlaceType,
    val address: Address,
    override val duration: Duration,
) : CommonItinerary(ItineraryType.PLACE) {
    companion object {
        fun fromDto(dto: PlaceDto) = with(dto) {
            PlaceItinerary(id, name, type, address, duration)
        }
    }
}