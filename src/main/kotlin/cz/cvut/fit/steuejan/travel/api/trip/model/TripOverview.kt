package cz.cvut.fit.steuejan.travel.api.trip.model

import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripOverviewDto
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class TripOverview(
    val id: Int,
    val name: String,
    val role: UserRole,
    val duration: Duration,
    val imageUrl: String?
) {
    companion object {
        fun fromDto(dto: TripOverviewDto) = with(dto.trip) {
            TripOverview(
                id = id,
                name = name,
                role = dto.tripUser.role,
                duration = duration,
                imageUrl = imageUrl
            )
        }
    }
}