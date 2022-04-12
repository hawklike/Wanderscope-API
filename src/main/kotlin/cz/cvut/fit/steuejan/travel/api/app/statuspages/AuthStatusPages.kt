package cz.cvut.fit.steuejan.travel.api.app.statuspages

import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.extension.preventH18bug
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
        preventH18bug()
        respond(Failure(Status.UNAUTHORIZED, FailureMessages.UNAUTHORIZED))
    }
}