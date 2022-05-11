package cz.cvut.fit.steuejan.travel.api.trip.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripDto
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class TripResponse(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val duration: Duration,
    val description: String?,
    val imageUrl: String?,
    val userRole: UserRole
) : Response by Success() {
    companion object {
        fun success(trip: TripDto, userRole: UserRole) = with(trip) {
            TripResponse(
                id = id,
                ownerId = ownerId,
                name = name,
                duration = duration,
                description = description,
                imageUrl = imageUrl,
                userRole = userRole
            )
        }
    }
}
