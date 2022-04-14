package cz.cvut.fit.steuejan.travel.api.trip.itinerary.model

import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
class DateItinerary(
    override val duration: Duration
) : CommonItinerary(ItineraryType.DATE)