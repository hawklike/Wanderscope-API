package cz.cvut.fit.steuejan.travel.api.trip.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
data class TripDateRequest(val duration: Duration) : Request {
    companion object {
        const val MISSING_PARAM =
            "Required 'duration': Duration aka Pair(startDate: DateTime?, endDate: DateTime?). DateTime is time representing string in ISO8601 format."
    }
}
