@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trips
import cz.cvut.fit.steuejan.travel.api.app.util.throwIfMissing
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import cz.cvut.fit.steuejan.travel.api.trip.model.GetTripsType
import cz.cvut.fit.steuejan.travel.api.user.controller.UserController
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.userRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWT_AUTHENTICATION) {
        val userController = controllerFactory.userController
        showUserTrips(userController)
    }
}

private fun Route.showUserTrips(userController: UserController) {
    get<Trips> {
        val response = when (it.scope) {
            GetTripsType.ALL -> {
                userController.showAllTrips(getUserId())
            }
            GetTripsType.UPCOMING -> {
                val date = it.date.throwIfMissing(it::date.name)
                userController.showUpcomingTrips(getUserId(), date)
            }
            GetTripsType.PAST -> {
                val date = it.date.throwIfMissing(it::date.name)
                userController.showPastTrips(getUserId(), date)
            }
        }
        respond(response)
    }
}