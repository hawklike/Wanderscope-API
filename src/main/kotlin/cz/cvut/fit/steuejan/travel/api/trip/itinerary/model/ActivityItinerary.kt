package cz.cvut.fit.steuejan.travel.api.trip.itinerary.model

import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityDto
import cz.cvut.fit.steuejan.travel.data.model.ActivityType
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
class ActivityItinerary(
    val id: Int,
    val name: String,
    val activity: ActivityType,
    override val duration: Duration,
    val address: Address
) : CommonItinerary(ItineraryType.ACTIVITY) {
    companion object {
        fun fromDto(dto: ActivityDto) = with(dto) {
            ActivityItinerary(
                id,
                name,
                type ?: ActivityType.OTHER,
                duration,
                address
            )
        }
    }
}