package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trip
import cz.cvut.fit.steuejan.travel.api.app.location.Trips
import cz.cvut.fit.steuejan.travel.api.app.util.throwIfMissing
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig.Companion.JWT_AUTHENTICATION
import cz.cvut.fit.steuejan.travel.api.trip.controller.TripController
import cz.cvut.fit.steuejan.travel.api.trip.model.GetTripsType
import cz.cvut.fit.steuejan.travel.api.trip.request.TripInvitationRequest
import cz.cvut.fit.steuejan.travel.api.trip.request.TripRequest
import cz.cvut.fit.steuejan.travel.api.user.controller.UserController
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.*
import org.koin.ktor.ext.inject


@KtorExperimentalLocationsAPI
fun Routing.tripRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWT_AUTHENTICATION) {
        val tripController = controllerFactory.tripController
        val userController = controllerFactory.userController
        val transportController = controllerFactory.transportController

        createTrip(tripController)
        deleteTrip(tripController)
        editTrip(tripController)
        inviteToTrip(tripController)

        showUserTrips(userController)
    }
}

@KtorExperimentalLocationsAPI
private fun Route.createTrip(tripController: TripController) {
    post<Trip> {
        val trip = receive<TripRequest>(TripRequest.MISSING_PARAM).toDto()
        respond(tripController.createTrip(getUserId(), trip))
    }
}

@KtorExperimentalLocationsAPI
private fun Route.deleteTrip(tripController: TripController) {
    delete<Trip> {
        val tripId = it.id.throwIfMissing(it::id.name)
        respond(tripController.deleteTrip(getUserId(), tripId))
    }
}

@KtorExperimentalLocationsAPI
private fun Route.editTrip(tripController: TripController) {
    put<Trip> {
        val tripId = it.id.throwIfMissing(it::id.name)
        val trip = receive<TripRequest>(TripRequest.MISSING_PARAM).toDto()
        respond(tripController.editTrip(getUserId(), tripId, trip))
    }
}

@KtorExperimentalLocationsAPI
private fun Route.inviteToTrip(tripController: TripController) {
    post<Trip.Invite> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val request = receive<TripInvitationRequest>(TripInvitationRequest.MISSING_PARAM)
        respond(tripController.invite(getUserId(), request.getTripInvitation(tripId)))
    }
}

@KtorExperimentalLocationsAPI
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