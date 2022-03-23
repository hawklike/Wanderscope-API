package cz.cvut.fit.steuejan.travel.api.account.response

import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.general.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.api.app.response.general.Status
import cz.cvut.fit.steuejan.travel.api.app.response.general.Success
import cz.cvut.fit.steuejan.travel.api.app.response.model.User

@kotlinx.serialization.Serializable
class ChangePasswordResponse(
    val user: User,
    val accessToken: String,
    val refreshToken: String
) : Response by Success() {
    companion object {
        fun success(user: User, accessToken: String, refreshToken: String) =
            ChangePasswordResponse(user, accessToken, refreshToken)

        fun failure() = Failure(Status.INTERNAL_ERROR, FailureMessages.PASSWORD_CHANGE_ERROR)
    }
}