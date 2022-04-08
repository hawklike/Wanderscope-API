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
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.controller.AccomodationController
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.request.AccomodationRequest
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.accomodationRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWTConfig.JWT_AUTHENTICATION) {
        val accomodationController = controllerFactory.accomodationController

        addAccomodation(accomodationController)
        showAccomodation(accomodationController)
        editAccomodation(accomodationController)
        deleteAccomodation(accomodationController)
    }
}

private fun Route.addAccomodation(accomodationController: AccomodationController) {
    post<Trip.Accomodation> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val accomodation = receive<AccomodationRequest>(AccomodationRequest.MISSING_PARAM).toDto()
        respond(accomodationController.add(getUserId(), tripId, accomodation))
    }
}

private fun Route.showAccomodation(accomodationController: AccomodationController) {
    get<Trip.Accomodation> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val accomodationId = it.accomodationId.throwIfMissing(it::accomodationId.name)
        respond(accomodationController.getAccomodation(getUserId(), tripId, accomodationId))
    }
}

private fun Route.editAccomodation(accomodationController: AccomodationController) {
    put<Trip.Accomodation> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val accomodationId = it.accomodationId.throwIfMissing(it::accomodationId.name)
        val transport = receive<AccomodationRequest>(AccomodationRequest.MISSING_PARAM).toDto()
        respond(accomodationController.edit(getUserId(), tripId, accomodationId, transport))
    }
}

private fun Route.deleteAccomodation(accomodationController: AccomodationController) {
    delete<Trip.Accomodation> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val accomodationId = it.accomodationId.throwIfMissing(it::accomodationId.name)
        respond(accomodationController.delete(getUserId(), tripId, accomodationId))
    }
}



