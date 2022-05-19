@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

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
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.controller.ItineraryController
import cz.cvut.fit.steuejan.travel.api.trip.model.GetTripsType
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.controller.AccommodationController
import cz.cvut.fit.steuejan.travel.api.trip.poi.activity.controller.ActivityController
import cz.cvut.fit.steuejan.travel.api.trip.poi.place.controller.PlaceController
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.controller.TransportController
import cz.cvut.fit.steuejan.travel.api.trip.request.*
import cz.cvut.fit.steuejan.travel.api.user.controller.UserController
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
        val userController = controllerFactory.userController
        val itineraryController = controllerFactory.itineraryController

        createTrip(tripController)
        getTrip(tripController)
        deleteTrip(tripController)
        editTrip(tripController)
        changeDate(tripController)

        inviteToTrip(tripController)
        leaveTrip(tripController)
        changeRole(tripController)
        removeRole(tripController)

        showDocuments(tripController)
        showUsers(tripController)
        showItinerary(itineraryController)
        showExpenseRooms(tripController)

        with(controllerFactory) {
            showTransports(transportController)
            showAccommodation(accommodationController)
            showActivities(activityController)
            showPlaces(placeController)
        }

        showUserTrips(userController)
    }
}

private fun Route.createTrip(tripController: TripController) {
    post<Trip> {
        val trip = receive<TripRequest>(TripRequest.MISSING_PARAM).toDto()
        respond(tripController.createTrip(getUserId(), trip))
    }
}

private fun Route.getTrip(tripController: TripController) {
    get<Trip> {
        val tripId = it.id.throwIfMissing(it::id.name)
        respond(tripController.getTrip(getUserId(), tripId))
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
        val duration = receive<TripDateRequest>(TripDateRequest.MISSING_PARAM).duration.validate()
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

private fun Route.showItinerary(itineraryController: ItineraryController) {
    get<Trip.Itinerary> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        respond(itineraryController.showItinerary(getUserId(), tripId))
    }
}

private fun Route.showExpenseRooms(tripController: TripController) {
    get<Trip.ExpenseRooms> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        respond(tripController.showExpenseRooms(getUserId(), tripId))
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
            GetTripsType.RECOMMENDED -> {
                val date = it.date.throwIfMissing(it::date.name)
                userController.getRecommendedTrip(getUserId(), date)
            }
        }
        respond(response)
    }
}

private fun Route.leaveTrip(tripController: TripController) {
    post<Trip.Leave> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        respond(tripController.leave(getUserId(), tripId))
    }
}

private fun Route.changeRole(tripController: TripController) {
    post<Trip.Role> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val changeRole = receive<ChangeRoleRequest>(ChangeRoleRequest.MISSING_PARAM).toModel()
        respond(tripController.changeRole(getUserId(), tripId, changeRole))
    }
}

private fun Route.removeRole(tripController: TripController) {
    delete<Trip.Role> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val removeRole = receive<RemoveRoleRequest>(RemoveRoleRequest.MISSING_PARAM).toModel()
        respond(tripController.changeRole(getUserId(), tripId, removeRole))
    }
}

private fun Route.showTransports(transportController: TransportController) {
    get<Trip.Transports> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        respond(transportController.showTransportsInTrip(getUserId(), tripId))
    }
}

private fun Route.showAccommodation(accommodationController: AccommodationController) {
    get<Trip.AllAccommodation> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        respond(accommodationController.showAccommodationInTrip(getUserId(), tripId))
    }
}

private fun Route.showActivities(activityController: ActivityController) {
    get<Trip.Activities> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        respond(activityController.showActivitiesInTrip(getUserId(), tripId))
    }
}

private fun Route.showPlaces(placeController: PlaceController) {
    get<Trip.Places> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        respond(placeController.showPlacesInTrip(getUserId(), tripId))
    }
}