@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getQuery
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trip
import cz.cvut.fit.steuejan.travel.api.app.util.throwIfMissing
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig
import cz.cvut.fit.steuejan.travel.api.trip.poi.place.controller.PlaceController
import cz.cvut.fit.steuejan.travel.api.trip.poi.place.request.PlaceRequest
import cz.cvut.fit.steuejan.travel.api.trip.poi.place.wiki.WikiSearchBundle
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.placeRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWTConfig.JWT_AUTHENTICATION) {
        val placeController = controllerFactory.placeController

        addPlace(placeController)
        showPlace(placeController)
        editPlace(placeController)
        deletePlace(placeController)
        showDocuments(placeController)
    }
}

private fun Route.addPlace(placeController: PlaceController) {
    post<Trip.Place> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val place = receive<PlaceRequest>(PlaceRequest.MISSING_PARAM).toDto()
        val search = getQuery("search")
        val wiki = if (search != null) WikiSearchBundle(this, search) else null
        respond(placeController.add(getUserId(), tripId, place, wiki))
    }
}

private fun Route.showPlace(placeController: PlaceController) {
    get<Trip.Place> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val placeId = it.placeId.throwIfMissing(it::placeId.name)
        respond(placeController.getPlace(getUserId(), tripId, placeId))
    }
}

private fun Route.editPlace(placeController: PlaceController) {
    put<Trip.Place> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val placeId = it.placeId.throwIfMissing(it::placeId.name)
        val place = receive<PlaceRequest>(PlaceRequest.MISSING_PARAM).toDto()
        val search = getQuery("search")
        val wiki = if (search != null) WikiSearchBundle(this, search) else null
        respond(placeController.edit(getUserId(), tripId, placeId, place, wiki))
    }
}

private fun Route.deletePlace(placeController: PlaceController) {
    delete<Trip.Place> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val placeId = it.placeId.throwIfMissing(it::placeId.name)
        respond(placeController.delete(getUserId(), tripId, placeId))
    }
}

private fun Route.showDocuments(placeController: PlaceController) {
    get<Trip.Place.Documents> {
        val tripId = it.place.trip.id.throwIfMissing(it.place.trip::id.name)
        val placeId = it.place.placeId.throwIfMissing(it.place::placeId.name)
        respond(placeController.showDocuments(getUserId(), tripId, placeId))
    }
}



