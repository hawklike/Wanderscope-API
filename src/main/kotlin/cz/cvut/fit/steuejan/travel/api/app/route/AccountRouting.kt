@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.account.controller.AccountController
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

fun Routing.accountRoutes() {
    val controllerFactory: ControllerFactory by inject()

    logout(controllerFactory.accountController)

    authenticate(JWT_AUTHENTICATION) {
        logoutAll(controllerFactory.accountController)
        changePassword(controllerFactory.accountController)
    }
}

private fun Route.logout(accountController: AccountController) {
    post<Account.Logout> {
        val request = receive<RefreshTokenRequest>(RefreshTokenRequest.MISSING_PARAM)
        respond(accountController.logout(request.refreshToken))
    }
}

private fun Route.logoutAll(accountController: AccountController) {
    post<Account.LogoutAll> {
        respond(accountController.logoutAllDevices(getUserId()))
    }
}

private fun Route.changePassword(accountController: AccountController) {
    post<Account.ChangePassword> {
        val request = receive<ChangePasswordRequest>(ChangePasswordRequest.MISSING_PARAM)
        respond(accountController.changePassword(getUserId(), request.toChangePassword(), addToDb = true))
    }
}