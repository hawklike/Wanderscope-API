package cz.cvut.fit.steuejan.travel.api.trip.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.model.TripOverview
import kotlinx.serialization.Serializable

@Serializable
data class TripOverviewListResponse(
    val trips: List<TripOverview>
) : Response by Success() {
    companion object {
        fun success(trips: List<TripOverview>) = TripOverviewListResponse(trips)
    }
}