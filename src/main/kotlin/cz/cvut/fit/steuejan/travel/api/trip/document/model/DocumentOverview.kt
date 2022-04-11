package cz.cvut.fit.steuejan.travel.api.trip.document.model

import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentDto
import cz.cvut.fit.steuejan.travel.data.model.DocumentType
import kotlinx.serialization.Serializable
import org.joda.time.DateTime


@Serializable
data class DocumentOverview(
    val id: Int,
    val tripId: Int,
    val transportId: Int?,
    val accommodationId: Int?,
    val activityId: Int?,
    val placeId: Int?,
    val ownerId: Int,
    val name: String,
    val type: DocumentType,
    @Serializable(with = DateTimeSerializer::class)
    val created: DateTime,
    val extension: String?
) {
    companion object {
        fun fromDto(document: DocumentDto) = with(document) {
            DocumentOverview(
                id = id,
                tripId = tripId,
                transportId = transportId,
                accommodationId = accomodationId,
                activityId = activityId,
                placeId = placeId,
                ownerId = userId,
                name = name,
                type = type,
                created = created,
                extension = extension
            )
        }
    }
}
