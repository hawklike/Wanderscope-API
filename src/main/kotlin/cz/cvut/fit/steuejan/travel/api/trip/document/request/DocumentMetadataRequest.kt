package cz.cvut.fit.steuejan.travel.api.trip.document.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentMetadata
import cz.cvut.fit.steuejan.travel.data.model.DocumentType
import kotlinx.serialization.Serializable

@Serializable
data class DocumentMetadataRequest(
    val name: String,
    val type: DocumentType,
    val key: String?
) : Request {
    fun toDocumentMetadata() = DocumentMetadata(name, type, key)

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, 'type': DocumentType aka [TEXT, IMAGE, DOCUMENT]."
    }
}