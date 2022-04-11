package cz.cvut.fit.steuejan.travel.api.auth.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val refreshToken: String) : Request {
    companion object {
        const val MISSING_PARAM = "Required 'refreshToken': String."
    }
}