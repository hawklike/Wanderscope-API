package cz.cvut.fit.steuejan.travel.api.auth.response

import cz.cvut.fit.steuejan.travel.api.app.response.general.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.api.app.response.general.Status
import cz.cvut.fit.steuejan.travel.api.app.response.general.Success
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
) : Response by Success() {
    companion object {
        fun success(accessToken: String, refreshToken: String) = AuthResponse(accessToken, refreshToken)
        fun forbidden(message: String, code: Int) = Failure(Status.FORBIDDEN, message, code)
    }
}