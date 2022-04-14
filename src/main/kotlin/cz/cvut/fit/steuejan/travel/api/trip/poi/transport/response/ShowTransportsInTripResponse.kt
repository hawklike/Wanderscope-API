package cz.cvut.fit.steuejan.travel.api.trip.poi.transport.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Serializable
data class ShowTransportsInTripResponse(
    val transports: List<TransportResponse>
) : Response by Success() {
    companion object {
        fun success(transports: List<TransportResponse>) = ShowTransportsInTripResponse(transports)
    }
}