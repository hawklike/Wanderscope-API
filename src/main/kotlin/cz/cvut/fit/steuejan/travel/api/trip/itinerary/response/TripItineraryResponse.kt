package cz.cvut.fit.steuejan.travel.api.trip.itinerary.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.model.CommonItinerary
import kotlinx.serialization.Serializable

@Serializable
data class TripItineraryResponse(
    val itinerary: List<CommonItinerary>
) : Response by Success() {
    companion object {
        fun success(itinerary: List<CommonItinerary>) = TripItineraryResponse(itinerary)
    }
}