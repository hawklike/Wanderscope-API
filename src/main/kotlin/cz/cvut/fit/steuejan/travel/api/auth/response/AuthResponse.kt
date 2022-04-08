package cz.cvut.fit.steuejan.travel.api.auth.response

import cz.cvut.fit.steuejan.travel.api.app.response.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
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