package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.extension.getQuery
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trip
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import cz.cvut.fit.steuejan.travel.api.trip.controller.TripController
import cz.cvut.fit.steuejan.travel.api.trip.request.TripRequest
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.routing.*
import org.koin.ktor.ext.inject


@KtorExperimentalLocationsAPI
fun Routing.tripRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWT_AUTHENTICATION) {
        val tripController = controllerFactory.tripController

        createTrip(tripController)
        deleteTrip(tripController)
    }
}

@KtorExperimentalLocationsAPI
fun Route.createTrip(tripController: TripController) {
    post<Trip> {
        val trip = receive<TripRequest>(TripRequest.MISSING_PARAM).toDto()
        respond(tripController.createTrip(getUserId(), trip))
    }
}

@KtorExperimentalLocationsAPI
fun Route.deleteTrip(tripController: TripController) {
    delete<Trip> {
        val tripId = getQuery("id").toIntOrNull()
            ?: throw BadRequestException(FailureMessages.TRIP_NOT_FOUND)
        respond(tripController.deleteTrip(getUserId(), tripId))
    }
}