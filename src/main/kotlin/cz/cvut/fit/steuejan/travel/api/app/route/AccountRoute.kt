package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.account.request.ChangePasswordRequest
import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Account
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import cz.cvut.fit.steuejan.travel.api.auth.request.RefreshTokenRequest
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.routing.*
import org.koin.ktor.ext.inject


@KtorExperimentalLocationsAPI
fun Route.accountRoutes() {
    val controllerFactory: ControllerFactory by inject()

    post<Account.Logout> {
        val request = receive<RefreshTokenRequest>(RefreshTokenRequest.MISSING_PARAM)
        val userAccountController = controllerFactory.userAccountController
        respond(userAccountController.logout(request.refreshToken))
    }

    authenticate(JWT_AUTHENTICATION) {
        val userAccountController = controllerFactory.userAccountController

        get<Account.LogoutAll> {
            respond(userAccountController.logoutAllDevices(getUserId()))
        }

        post<Account.ChangePassword> {
            val request = receive<ChangePasswordRequest>(ChangePasswordRequest.MISSING_PARAM)
            respond(userAccountController.changePassword(getUserId(), request.toChangePassword(), addToDb = true))
        }
    }
}