package cz.cvut.fit.steuejan.travel.api.trip.model

import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import cz.cvut.fit.steuejan.travel.data.dto.TripOverviewDto
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class TripOverview(
    val id: Int,
    val name: String,
    val canEdit: Boolean,
    @Serializable(with = DateTimeSerializer::class)
    val startDate: DateTime?,
    @Serializable(with = DateTimeSerializer::class)
    val endDate: DateTime?,
    val imageUrl: String?
) {
    companion object {
        fun fromDto(tripOverview: TripOverviewDto) = with(tripOverview) {
            with(trip) {
                TripOverview(
                    id = id,
                    name = name,
                    canEdit = tripUser.canEdit,
                    startDate = duration.startDate,
                    endDate = duration.endDate,
                    imageUrl = imageUrl
                )
            }
        }
    }
}