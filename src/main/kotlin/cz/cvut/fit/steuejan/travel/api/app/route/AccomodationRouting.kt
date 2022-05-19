@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trip
import cz.cvut.fit.steuejan.travel.api.app.util.throwIfMissing
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.controller.AccommodationController
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.request.AccommodationRequest
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.accomodationRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWTConfig.JWT_AUTHENTICATION) {
        val accomodationController = controllerFactory.accommodationController

        addAccomodation(accomodationController)
        showAccomodation(accomodationController)
        editAccomodation(accomodationController)
        deleteAccomodation(accomodationController)
        showDocuments(accomodationController)
    }
}

private fun Route.addAccomodation(accommodationController: AccommodationController) {
    post<Trip.Accommodation> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val accomodation = receive<AccommodationRequest>(AccommodationRequest.MISSING_PARAM).toDto()
        respond(accommodationController.add(getUserId(), tripId, accomodation))
    }
}

private fun Route.showAccomodation(accommodationController: AccommodationController) {
    get<Trip.Accommodation> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val accomodationId = it.accommodationId.throwIfMissing(it::accommodationId.name)
        respond(accommodationController.getAccommodation(getUserId(), tripId, accomodationId))
    }
}

private fun Route.editAccomodation(accommodationController: AccommodationController) {
    put<Trip.Accommodation> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val accomodationId = it.accommodationId.throwIfMissing(it::accommodationId.name)
        val transport = receive<AccommodationRequest>(AccommodationRequest.MISSING_PARAM).toDto()
        respond(accommodationController.edit(getUserId(), tripId, accomodationId, transport))
    }
}

private fun Route.deleteAccomodation(accommodationController: AccommodationController) {
    delete<Trip.Accommodation> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val accomodationId = it.accommodationId.throwIfMissing(it::accommodationId.name)
        respond(accommodationController.delete(getUserId(), tripId, accomodationId))
    }
}

private fun Route.showDocuments(accommodationController: AccommodationController) {
    get<Trip.Accommodation.Documents> {
        val tripId = it.accommodation.trip.id.throwIfMissing(it.accommodation.trip::id.name)
        val accomodationId = it.accommodation.accommodationId.throwIfMissing(it.accommodation::accommodationId.name)
        respond(accommodationController.showDocuments(getUserId(), tripId, accomodationId))
    }
}



