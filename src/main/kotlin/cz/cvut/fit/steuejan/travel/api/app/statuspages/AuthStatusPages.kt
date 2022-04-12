package cz.cvut.fit.steuejan.travel.api.app.statuspages

import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.extension.getFile
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.response.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.auth.exception.AuthenticationException
import cz.cvut.fit.steuejan.travel.api.auth.exception.AuthorizationException
import cz.cvut.fit.steuejan.travel.api.auth.response.AuthResponse
import io.ktor.features.*
import io.ktor.http.*

fun StatusPages.Configuration.authStatusPages() {
    exception<AuthenticationException> {
        respond(AuthResponse.forbidden(it.message, it.code))
    }

    exception<AuthorizationException> {
        respond(AuthResponse.forbidden(it.message, it.code))
    }

    status(HttpStatusCode.Unauthorized) {
        try {
            getFile()
        } catch (ex: Exception) {
            //this code here is to fix bug on Heroku router
            //https://stackoverflow.com/a/63057984/9723204
        }
        respond(Failure(Status.UNAUTHORIZED, FailureMessages.UNAUTHORIZED))
    }
}