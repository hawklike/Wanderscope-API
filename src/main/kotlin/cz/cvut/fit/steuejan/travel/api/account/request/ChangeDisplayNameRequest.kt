package cz.cvut.fit.steuejan.travel.api.account.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import kotlinx.serialization.Serializable

@Serializable
data class ChangeDisplayNameRequest(
    val displayName: String
) : Request {
    companion object {
        const val MISSING_PARAM = "Required 'displayName': String."
    }
}
