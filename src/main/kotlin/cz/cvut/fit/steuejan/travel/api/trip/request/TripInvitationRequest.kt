package cz.cvut.fit.steuejan.travel.api.trip.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.trip.model.TripInvitation
import cz.cvut.fit.steuejan.travel.data.model.UserRole
import cz.cvut.fit.steuejan.travel.data.model.Username
import kotlinx.serialization.Serializable

@Serializable
data class TripInvitationRequest(
    val username: String,
    val role: UserRole
) : Request {
    fun getTripInvitation(tripId: Int) = TripInvitation(Username(username), tripId, role)

    companion object {
        const val MISSING_PARAM = "Required 'username', 'role': UserRole aka [ADMIN, EDITOR, VIEWER]."
    }
}