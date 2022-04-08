package cz.cvut.fit.steuejan.travel.api.trip.document.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentMetadata
import kotlinx.serialization.Serializable

@Serializable
data class DocumentMetadataRequest(
    val name: String,
    val extension: String,
    val key: String?
) : Request {
    fun toDocumentMetadata() = DocumentMetadata(name, extension, key)

    companion object {
        const val MISSING_PARAM = "Required 'name': String, 'extension': String representing file extension."
    }
}