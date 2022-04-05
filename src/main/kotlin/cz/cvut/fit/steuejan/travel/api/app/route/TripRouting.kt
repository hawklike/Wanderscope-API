package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trip
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import cz.cvut.fit.steuejan.travel.api.trip.request.TripRequest
import cz.cvut.fit.steuejan.travel.api.user.controller.UserController
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.routing.*
import org.koin.ktor.ext.inject


@KtorExperimentalLocationsAPI
fun Routing.tripRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWT_AUTHENTICATION) {
        createTrip(controllerFactory.userController)
    }
}

@KtorExperimentalLocationsAPI
fun Route.createTrip(userController: UserController) {
    post<Trip> {
        val trip = receive<TripRequest>(TripRequest.MISSING_PARAM).toDto()
        respond(userController.createTrip(getUserId(), trip))
    }
}