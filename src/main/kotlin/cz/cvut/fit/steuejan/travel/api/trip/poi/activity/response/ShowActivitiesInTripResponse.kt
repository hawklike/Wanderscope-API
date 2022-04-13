package cz.cvut.fit.steuejan.travel.api.trip.poi.activity.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Serializable
data class ShowActivitiesInTripResponse(
    val activities: List<ActivityResponse>
) : Response by Success() {
    companion object {
        fun success(activities: List<ActivityResponse>) = ShowActivitiesInTripResponse(activities)
    }
}