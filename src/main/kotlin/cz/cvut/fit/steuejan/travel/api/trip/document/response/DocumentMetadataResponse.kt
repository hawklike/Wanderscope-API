package cz.cvut.fit.steuejan.travel.api.trip.document.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Serializable
data class DocumentMetadataResponse(
    val tripId: Int,
    val documentId: Int
) : Response by Success(Status.CREATED) {
    companion object {
        fun success(tripId: Int, documentId: Int) = DocumentMetadataResponse(tripId, documentId)
    }
}