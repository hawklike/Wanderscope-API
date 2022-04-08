package cz.cvut.fit.steuejan.travel.api.trip.document.request

import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.auth.util.Encryptor
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentMetadata
import kotlinx.serialization.Serializable

@Serializable
data class DocumentMetadataRequest(
    val name: String,
    val extension: String,
    val key: String?
) : Request {
    fun toDocumentMetadata(encryptor: Encryptor, validator: Validator): DocumentMetadata {
        val hashedKey = key?.let {
            validator.validatePassword(key, "key")
            encryptor.hashPassword(it)
        }
        return DocumentMetadata(name, extension, hashedKey)
    }

    companion object {
        const val MISSING_PARAM = "Required 'name': String, 'extension': String representing file extension."
    }
}