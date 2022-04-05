package cz.cvut.fit.steuejan.travel.api.trip.request

import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.Duration
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class TripRequest(
    val name: String,
    @Serializable(DateTimeSerializer::class)
    val startDate: DateTime?,
    @Serializable(DateTimeSerializer::class)
    val endDate: DateTime?,
    val description: String?,
    val imageUrl: String?
) : Request {
    fun toDto() = TripDto(
        id = Dto.UNKNOWN_ID,
        ownerId = Dto.UNKNOWN_ID,
        name = name,
        duration = Duration(startDate, endDate),
        description = description,
        imageUrl = imageUrl,
        linkView = null,
        linkEdit = null
    )

    companion object {
        const val MISSING_PARAM = "Required 'name': String."
    }
}
