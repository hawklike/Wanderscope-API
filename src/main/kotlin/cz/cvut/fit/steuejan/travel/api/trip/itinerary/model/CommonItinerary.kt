package cz.cvut.fit.steuejan.travel.api.trip.itinerary.model

import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
sealed class CommonItinerary(
    val type: ItineraryType
) {
    abstract val duration: Duration
}