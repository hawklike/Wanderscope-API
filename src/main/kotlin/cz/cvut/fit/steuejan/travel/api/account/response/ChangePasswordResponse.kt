package cz.cvut.fit.steuejan.travel.api.account.response

import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
class ChangePasswordResponse(
    val accessToken: String,
    val refreshToken: String
) : Response by Success() {
    companion object {
        fun success(accessToken: String, refreshToken: String) = ChangePasswordResponse(accessToken, refreshToken)
        fun failure() = Failure(Status.INTERNAL_ERROR, FailureMessages.PASSWORD_CHANGE_ERROR)
    }
}