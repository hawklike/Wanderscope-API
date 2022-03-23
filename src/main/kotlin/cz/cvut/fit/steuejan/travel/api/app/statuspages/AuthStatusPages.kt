package cz.cvut.fit.steuejan.travel.api.app.statuspages

import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.auth.exception.AuthenticationException
import cz.cvut.fit.steuejan.travel.api.auth.exception.AuthorizationException
import cz.cvut.fit.steuejan.travel.api.auth.response.AuthResponse
import io.ktor.features.*

fun StatusPages.Configuration.authStatusPages() {
    exception<AuthenticationException> {
        respond(AuthResponse.forbidden(it.message, it.code))
    }

    exception<AuthorizationException> {
        respond(AuthResponse.forbidden(it.message, it.code))
    }
}