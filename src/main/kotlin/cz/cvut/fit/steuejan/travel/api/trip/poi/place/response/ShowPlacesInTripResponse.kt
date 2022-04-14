package cz.cvut.fit.steuejan.travel.api.trip.poi.place.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Serializable
data class ShowPlacesInTripResponse(
    val places: List<PlaceResponse>
) : Response by Success() {
    companion object {
        fun success(places: List<PlaceResponse>) = ShowPlacesInTripResponse(places)
    }
}