package cz.cvut.fit.steuejan.travel.api.trip.model

import cz.cvut.fit.steuejan.travel.data.dto.TripOverviewDto
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
data class TripOverview(
    val id: Int,
    val name: String,
    val canEdit: Boolean,
    val duration: Duration,
    val imageUrl: String?
) {
    companion object {
        fun fromDto(tripOverview: TripOverviewDto) = with(tripOverview) {
            with(trip) {
                TripOverview(
                    id = id,
                    name = name,
                    canEdit = tripUser.canEdit,
                    duration = duration,
                    imageUrl = imageUrl
                )
            }
        }
    }
}