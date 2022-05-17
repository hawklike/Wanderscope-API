package cz.cvut.fit.steuejan.travel.api.trip.poi.activity.request

import cz.cvut.fit.steuejan.travel.api.trip.poi.request.PointOfInterestRequest
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.ActivityType
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
data class ActivityRequest(
    override val name: String,
    override val duration: Duration?,
    val type: ActivityType?,
    val address: Address?,
    val mapLink: String?,
    val description: String?,
    val website: String?
) : PointOfInterestRequest<ActivityDto>() {

    override fun toDto() = ActivityDto(
        id = Dto.UNKNOWN_ID,
        tripId = Dto.UNKNOWN_ID,
        duration = duration?.validate() ?: Duration(),
        name = name,
        type = type,
        address = address ?: Address(),
        mapLink = mapLink,
        description = description,
        website = website
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, optional param 'type' is of type ActivityType aka [HIKING, CYCLING, SKIING, RUNNING, KAYAK, SWIMMING, CLIMBING, CROSS_COUNTRY, OTHER]."
    }
}
