package cz.cvut.fit.steuejan.travel.api.trip.itinerary.model

import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccommodationDto
import cz.cvut.fit.steuejan.travel.data.model.AccommodationType
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
class AccommodationItinerary(
    val id: Int,
    val name: String,
    val accommodation: AccommodationType,
    val address: Address,
    override val duration: Duration,
) : CommonItinerary(ItineraryType.ACCOMMODATION) {
    companion object {
        fun fromDto(dto: AccommodationDto) = with(dto) {
            AccommodationItinerary(id, name, type, address, duration)
        }
    }
}