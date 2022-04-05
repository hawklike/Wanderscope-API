package cz.cvut.fit.steuejan.travel.api.app.plugin

import cz.cvut.fit.steuejan.travel.api.app.di.factory.JWTControllerFactory
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import cz.cvut.fit.steuejan.travel.api.auth.token.AccessToken
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val jwtFactory: JWTControllerFactory by inject()

    authentication {
        jwt(name = JWT_AUTHENTICATION) {
            //returns 401-unauthorized if access token is invalid or 'sub' is missing
            verifier(jwtFactory.jwtController.getVerifier(AccessToken()))
            validate {
                val username = it.payload.subject
                if (username != null) {
                    JWTPrincipal(it.payload)
                } else {
                    null
                }
            }
        }
    }
}
