package cz.cvut.fit.steuejan.travel.api.trip.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.trip.model.TripInvitation
import cz.cvut.fit.steuejan.travel.data.model.Username
import kotlinx.serialization.Serializable

@Serializable
data class TripInvitationRequest(
    val username: String,
    val tripId: Int,
    val canEdit: Boolean
) : Request {
    fun getTripInvitation() = TripInvitation(Username(username), tripId, canEdit)

    companion object {
        const val MISSING_PARAM = "Required 'username': String, 'tripId': Int, 'canEdit': Boolean."
    }
}