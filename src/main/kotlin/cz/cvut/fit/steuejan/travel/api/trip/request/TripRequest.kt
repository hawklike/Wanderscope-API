package cz.cvut.fit.steuejan.travel.api.trip.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable

@Serializable
data class TripRequest(
    val name: String,
    val duration: Duration?,
    val description: String?,
    val imageUrl: String?
) : Request {
    fun toDto() = TripDto(
        id = Dto.UNKNOWN_ID,
        ownerId = Dto.UNKNOWN_ID,
        name = name,
        duration = duration ?: Duration(null, null),
        description = description,
        imageUrl = imageUrl,
        linkView = null,
        linkEdit = null
    )

    companion object {
        const val MISSING_PARAM = "Required 'name': String."
    }
}
