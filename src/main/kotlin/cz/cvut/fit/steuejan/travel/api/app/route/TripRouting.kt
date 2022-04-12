@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trip
import cz.cvut.fit.steuejan.travel.api.app.util.throwIfMissing
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import cz.cvut.fit.steuejan.travel.api.trip.controller.TripController
import cz.cvut.fit.steuejan.travel.api.trip.request.TripDateRequest
import cz.cvut.fit.steuejan.travel.api.trip.request.TripInvitationRequest
import cz.cvut.fit.steuejan.travel.api.trip.request.TripRequest
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.tripRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWT_AUTHENTICATION) {
        val tripController = controllerFactory.tripController

        createTrip(tripController)
        deleteTrip(tripController)
        editTrip(tripController)
        inviteToTrip(tripController)
        changeDate(tripController)

        showDocuments(tripController)
        showUsers(tripController)
    }
}

private fun Route.createTrip(tripController: TripController) {
    post<Trip> {
        val trip = receive<TripRequest>(TripRequest.MISSING_PARAM).toDto()
        respond(tripController.createTrip(getUserId(), trip))
    }
}

private fun Route.deleteTrip(tripController: TripController) {
    delete<Trip> {
        val tripId = it.id.throwIfMissing(it::id.name)
        respond(tripController.deleteTrip(getUserId(), tripId))
    }
}

private fun Route.editTrip(tripController: TripController) {
    put<Trip> {
        val tripId = it.id.throwIfMissing(it::id.name)
        val trip = receive<TripRequest>(TripRequest.MISSING_PARAM).toDto()
        respond(tripController.editTrip(getUserId(), tripId, trip))
    }
}

private fun Route.inviteToTrip(tripController: TripController) {
    post<Trip.Invite> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val request = receive<TripInvitationRequest>(TripInvitationRequest.MISSING_PARAM)
        respond(tripController.invite(getUserId(), request.getTripInvitation(tripId)))
    }
}

private fun Route.changeDate(tripController: TripController) {
    put<Trip.Date> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val duration = receive<TripDateRequest>(TripDateRequest.MISSING_PARAM).duration
        respond(tripController.changeDate(getUserId(), tripId, duration))
    }
}

private fun Route.showDocuments(tripController: TripController) {
    get<Trip.Documents> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        respond(tripController.showDocuments(getUserId(), tripId))
    }
}

private fun Route.showUsers(tripController: TripController) {
    get<Trip.Users> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        respond(tripController.showUsers(getUserId(), tripId, it.role))
    }
}