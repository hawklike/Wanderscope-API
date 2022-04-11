package cz.cvut.fit.steuejan.travel.api.auth.request

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.model.Credentials
import cz.cvut.fit.steuejan.travel.data.model.EmailLogin
import cz.cvut.fit.steuejan.travel.data.model.Username
import kotlinx.serialization.Serializable

@Serializable
data class EmailRegistrationRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
) : Request {
    fun getCredentials(): Credentials<EmailLogin> {
        if (password == confirmPassword) {
            return Credentials(Username(username), EmailLogin(email, password))
        } else {
            throw BadRequestException(FailureMessages.PASSWORDS_DONT_MATCH)
        }
    }

    companion object {
        const val MISSING_PARAM =
            "Required 'username': String, 'email': String, 'password': String and 'confirmPassword': String."
    }
}
