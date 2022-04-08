package cz.cvut.fit.steuejan.travel.api.account.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordResponse(
    val accessToken: String,
    val refreshToken: String
) : Response by Success() {
    companion object {
        fun success(accessToken: String, refreshToken: String) = ChangePasswordResponse(accessToken, refreshToken)
    }
}