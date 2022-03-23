package cz.cvut.fit.steuejan.travel.api.account.response

import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.general.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.api.app.response.general.Status
import cz.cvut.fit.steuejan.travel.api.app.response.general.Success

@kotlinx.serialization.Serializable
class LogoutResponse : Response by Success(Status.NO_CONTENT) {
    companion object {
        fun success() = LogoutResponse()
        fun failed() = Failure(Status.BAD_REQUEST, FailureMessages.ALL_DEVICES_LOGOUT_ERROR)
    }
}