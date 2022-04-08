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
import cz.cvut.fit.steuejan.travel.api.trip.document.controller.DocumentController
import cz.cvut.fit.steuejan.travel.api.trip.document.request.DocumentKeyRequest
import cz.cvut.fit.steuejan.travel.api.trip.document.request.DocumentMetadataRequest
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.controller.AccomodationController
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.request.AccomodationRequest
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.documentRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWTConfig.JWT_AUTHENTICATION) {
        val documentController = controllerFactory.documentController

        saveDocumentMetadataInTrip(documentController)
        setDocumentKeyInTrip(documentController)
    }
}

private fun Route.saveDocumentMetadataInTrip(documentController: DocumentController) {
    post<Trip.Document> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val request = receive<DocumentMetadataRequest>(DocumentMetadataRequest.MISSING_PARAM)
        val metadata = request.toDocumentMetadata()
        respond(documentController.saveMetadata(getUserId(), tripId, metadata))
    }
}

private fun Route.setDocumentKeyInTrip(documentController: DocumentController) {
    put<Trip.Document.Key> {
        val tripId = it.document.trip.id.throwIfMissing(it.document.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val key = receive<DocumentKeyRequest>(DocumentKeyRequest.MISSING_PARAM).key
        respond(documentController.setKey(getUserId(), tripId, documentId, key))
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



