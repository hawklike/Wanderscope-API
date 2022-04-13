package cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Serializable
data class ShowAccommodationInTripResponse(
    val accommodation: List<AccommodationResponse>
) : Response by Success() {
    companion object {
        fun success(accommodation: List<AccommodationResponse>) = ShowAccommodationInTripResponse(accommodation)
    }
}