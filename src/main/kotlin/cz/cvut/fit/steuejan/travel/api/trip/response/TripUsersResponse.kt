package cz.cvut.fit.steuejan.travel.api.trip.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.model.TripUser
import kotlinx.serialization.Serializable

@Serializable
data class TripUsersResponse(
    val users: List<TripUser>
) : Response by Success() {
    companion object {
        fun success(users: List<TripUser>) = TripUsersResponse(users)
    }
}
