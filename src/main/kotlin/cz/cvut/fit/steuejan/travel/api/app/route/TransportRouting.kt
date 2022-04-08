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
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.controller.TransportController
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.request.TransportRequest
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.transportRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWTConfig.JWT_AUTHENTICATION) {
        val transportController = controllerFactory.transportController

        addTransport(transportController)
        showTransport(transportController)
        editTransport(transportController)
        deleteTransport(transportController)
    }
}

private fun Route.addTransport(transportController: TransportController) {
    post<Trip.Transport> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val transport = receive<TransportRequest>(TransportRequest.MISSING_PARAM).toDto()
        respond(transportController.add(getUserId(), tripId, transport))
    }
}

private fun Route.showTransport(transportController: TransportController) {
    get<Trip.Transport> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val transportId = it.transportId.throwIfMissing(it::transportId.name)
        respond(transportController.getTransport(getUserId(), tripId, transportId))
    }
}

private fun Route.editTransport(transportController: TransportController) {
    put<Trip.Transport> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val transportId = it.transportId.throwIfMissing(it::transportId.name)
        val transport = receive<TransportRequest>(TransportRequest.MISSING_PARAM).toDto()
        respond(transportController.edit(getUserId(), tripId, transportId, transport))
    }
}

private fun Route.deleteTransport(transportController: TransportController) {
    delete<Trip.Transport> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val transportId = it.transportId.throwIfMissing(it::transportId.name)
        respond(transportController.delete(getUserId(), tripId, transportId))
    }
}



