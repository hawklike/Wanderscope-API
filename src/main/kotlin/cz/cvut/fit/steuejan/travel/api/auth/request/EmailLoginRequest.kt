package cz.cvut.fit.steuejan.travel.api.auth.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.model.EmailLogin

@kotlinx.serialization.Serializable
data class EmailLoginRequest(val email: String, val password: String) : Request {
    fun getLogin() = EmailLogin(email, password)

    companion object {
        const val MISSING_PARAM = "Required 'email': String and 'password': String."
    }
}