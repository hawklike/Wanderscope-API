package cz.cvut.fit.steuejan.travel.api.trip.document.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import kotlinx.serialization.Serializable

@Serializable
data class DocumentKeyRequest(
    val key: String
) : Request {
    companion object {
        const val MISSING_PARAM = "Required 'key': String."
    }
}
