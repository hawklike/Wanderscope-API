@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getFile
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trip
import cz.cvut.fit.steuejan.travel.api.app.util.throwIfMissing
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig
import cz.cvut.fit.steuejan.travel.api.trip.document.controller.DocumentController
import cz.cvut.fit.steuejan.travel.api.trip.document.model.FileWrapper
import cz.cvut.fit.steuejan.travel.api.trip.document.request.DocumentKeyRequest
import cz.cvut.fit.steuejan.travel.api.trip.document.request.DocumentMetadataRequest
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Parameters.FileName
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

const val DOCUMENT_KEY_HEADER = "Wanderscope-Document-Key"

fun Routing.documentRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWTConfig.JWT_AUTHENTICATION) {
        val documentController = controllerFactory.documentController

        saveDocumentMetadataInTrip(documentController)
        saveDataInTrip(documentController)
        getDataInTrip(documentController)
        setDocumentKeyInTrip(documentController)

        saveDocumentMetadataInTransport(documentController)
        saveDocumentMetadataInAccommodation(documentController)
        saveDocumentMetadataInActivity(documentController)
        saveDocumentMetadataInPlace(documentController)

        saveDataInTransport(documentController)
        saveDataInAccommodation(documentController)
        saveDataInActivity(documentController)
        saveDataInPlace(documentController)

        getDataInTransport(documentController)
        getDataInAccommodation(documentController)
        getDataInActivity(documentController)
        getDataInPlace(documentController)

        setDocumentKeyInTransport(documentController)
        setDocumentKeyInAccommodation(documentController)
        setDocumentKeyInActivity(documentController)
        setDocumentKeyInPlace(documentController)
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

private fun Route.saveDataInTrip(documentController: DocumentController) {
    post<Trip.Document.Data> {
        val tripId = it.document.trip.id.throwIfMissing(it.document.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        respond(documentController.saveData(getUserId(), tripId, documentId, getFile()))
    }
}

private fun Route.getDataInTrip(documentController: DocumentController) {
    get<Trip.Document.Data> {
        val tripId = it.document.trip.id.throwIfMissing(it.document.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val key = call.request.header(DOCUMENT_KEY_HEADER)
        val file = documentController.getData(getUserId(), tripId, documentId, key)
        getData(this, file)
    }
}

private suspend fun getData(context: PipelineContext<Unit, ApplicationCall>, file: FileWrapper) {
    with(context) {
        //downloadable from browser and overrides the filename with the original name
        call.response.header(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Attachment.withParameter(FileName, file.originalName).toString()
        )
        call.respondBytes(file.rawData)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.getDataInTransport(documentController: DocumentController) {
    get<Trip.Transport.Document.Data> {
        val tripId = it.document.transport.trip.id.throwIfMissing(it.document.transport.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val transportId = it.document.transport.transportId.throwIfMissing(it.document.transport::transportId.name)
        getDataInPoi(this, documentController, tripId, transportId, documentId, TRANSPORT)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.getDataInAccommodation(documentController: DocumentController) {
    get<Trip.Accomodation.Document.Data> {
        val tripId = it.document.accommodation.trip.id.throwIfMissing(it.document.accommodation.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val transportId =
            it.document.accommodation.accomodationId.throwIfMissing(it.document.accommodation::accomodationId.name)
        getDataInPoi(this, documentController, tripId, transportId, documentId, ACCOMMODATION)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.getDataInActivity(documentController: DocumentController) {
    get<Trip.Activity.Document.Data> {
        val tripId = it.document.activity.trip.id.throwIfMissing(it.document.activity.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val activityId = it.document.activity.activityId.throwIfMissing(it.document.activity::activityId.name)
        getDataInPoi(this, documentController, tripId, activityId, documentId, ACTIVITY)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.getDataInPlace(documentController: DocumentController) {
    get<Trip.Place.Document.Data> {
        val tripId = it.document.place.trip.id.throwIfMissing(it.document.place.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val placeId = it.document.place.placeId.throwIfMissing(it.document.place::placeId.name)
        getDataInPoi(this, documentController, tripId, placeId, documentId, PLACE)
    }
}

private suspend fun getDataInPoi(
    context: PipelineContext<Unit, ApplicationCall>,
    documentController: DocumentController,
    tripId: Int,
    poiId: Int,
    documentId: Int,
    poiType: PointOfInterestType
) {
    with(context) {
        val key = call.request.header(DOCUMENT_KEY_HEADER)
        val file = documentController.getData(getUserId(), tripId, poiId, documentId, key, poiType)
        getData(this, file)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.saveDataInTransport(documentController: DocumentController) {
    post<Trip.Transport.Document.Data> {
        val tripId = it.document.transport.trip.id.throwIfMissing(it.document.transport.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val transportId = it.document.transport.transportId.throwIfMissing(it.document.transport::transportId.name)
        val response = documentController.saveData(getUserId(), tripId, transportId, documentId, getFile(), TRANSPORT)
        respond(response)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.saveDataInAccommodation(documentController: DocumentController) {
    post<Trip.Accomodation.Document.Data> {
        val tripId = it.document.accommodation.trip.id.throwIfMissing(it.document.accommodation.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val accommodationId =
            it.document.accommodation.accomodationId.throwIfMissing(it.document.accommodation::accomodationId.name)
        val response =
            documentController.saveData(getUserId(), tripId, accommodationId, documentId, getFile(), ACCOMMODATION)
        respond(response)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.saveDataInActivity(documentController: DocumentController) {
    post<Trip.Activity.Document.Data> {
        val tripId = it.document.activity.trip.id.throwIfMissing(it.document.activity.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val activityId = it.document.activity.activityId.throwIfMissing(it.document.activity::activityId.name)
        val response = documentController.saveData(getUserId(), tripId, activityId, documentId, getFile(), ACTIVITY)
        respond(response)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.saveDataInPlace(documentController: DocumentController) {
    post<Trip.Place.Document.Data> {
        val tripId = it.document.place.trip.id.throwIfMissing(it.document.place.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val placeId = it.document.place.placeId.throwIfMissing(it.document.place::placeId.name)
        val response = documentController.saveData(getUserId(), tripId, placeId, documentId, getFile(), PLACE)
        respond(response)
    }
}

private fun Route.saveDocumentMetadataInTransport(documentController: DocumentController) {
    post<Trip.Transport.Document> {
        val tripId = it.transport.trip.id.throwIfMissing(it.transport.trip::id.name)
        val poiId = it.transport.transportId.throwIfMissing(it.transport::transportId.name)
        saveDocumentMetadataInPoi(this, documentController, tripId, poiId, TRANSPORT)
    }
}

private fun Route.saveDocumentMetadataInAccommodation(documentController: DocumentController) {
    post<Trip.Accomodation.Document> {
        val tripId = it.accommodation.trip.id.throwIfMissing(it.accommodation.trip::id.name)
        val poiId = it.accommodation.accomodationId.throwIfMissing(it.accommodation::accomodationId.name)
        saveDocumentMetadataInPoi(this, documentController, tripId, poiId, ACCOMMODATION)
    }
}

private fun Route.saveDocumentMetadataInActivity(documentController: DocumentController) {
    post<Trip.Activity.Document> {
        val tripId = it.activity.trip.id.throwIfMissing(it.activity.trip::id.name)
        val poiId = it.activity.activityId.throwIfMissing(it.activity::activityId.name)
        saveDocumentMetadataInPoi(this, documentController, tripId, poiId, ACTIVITY)
    }
}

private fun Route.saveDocumentMetadataInPlace(documentController: DocumentController) {
    post<Trip.Place.Document> {
        val tripId = it.place.trip.id.throwIfMissing(it.place.trip::id.name)
        val poiId = it.place.placeId.throwIfMissing(it.place::placeId.name)
        saveDocumentMetadataInPoi(this, documentController, tripId, poiId, PLACE)
    }
}

private suspend fun saveDocumentMetadataInPoi(
    context: PipelineContext<Unit, ApplicationCall>,
    documentController: DocumentController,
    tripId: Int,
    poiId: Int,
    poiType: PointOfInterestType
) {
    val request = context.receive<DocumentMetadataRequest>(DocumentMetadataRequest.MISSING_PARAM)
    val metadata = request.toDocumentMetadata()
    context.respond(documentController.saveMetadata(context.getUserId(), tripId, metadata, poiId, poiType))
}

private fun Route.setDocumentKeyInTrip(documentController: DocumentController) {
    put<Trip.Document.Key> {
        val tripId = it.document.trip.id.throwIfMissing(it.document.trip::id.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        val key = receive<DocumentKeyRequest>(DocumentKeyRequest.MISSING_PARAM).key
        respond(documentController.setKey(getUserId(), tripId, documentId, key))
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.setDocumentKeyInTransport(documentController: DocumentController) {
    put<Trip.Transport.Document.Key> {
        val tripId = it.document.transport.trip.id.throwIfMissing(it.document.transport.trip::id.name)
        val poiId = it.document.transport.transportId.throwIfMissing(it.document.transport::transportId.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        setDocumentKeyInPoi(this, documentController, tripId, poiId, documentId, TRANSPORT)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.setDocumentKeyInAccommodation(documentController: DocumentController) {
    put<Trip.Accomodation.Document.Key> {
        val tripId = it.document.accommodation.trip.id.throwIfMissing(it.document.accommodation.trip::id.name)
        val poiId =
            it.document.accommodation.accomodationId.throwIfMissing(it.document.accommodation::accomodationId.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        setDocumentKeyInPoi(this, documentController, tripId, poiId, documentId, ACCOMMODATION)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.setDocumentKeyInActivity(documentController: DocumentController) {
    put<Trip.Activity.Document.Key> {
        val tripId = it.document.activity.trip.id.throwIfMissing(it.document.activity.trip::id.name)
        val poiId = it.document.activity.activityId.throwIfMissing(it.document.activity::activityId.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        setDocumentKeyInPoi(this, documentController, tripId, poiId, documentId, ACTIVITY)
    }
}

@Suppress("DuplicatedCode") //IDE misinterpreted duplication
private fun Route.setDocumentKeyInPlace(documentController: DocumentController) {
    put<Trip.Place.Document.Key> {
        val tripId = it.document.place.trip.id.throwIfMissing(it.document.place.trip::id.name)
        val poiId = it.document.place.placeId.throwIfMissing(it.document.place::placeId.name)
        val documentId = it.document.documentId.throwIfMissing(it.document::documentId.name)
        setDocumentKeyInPoi(this, documentController, tripId, poiId, documentId, PLACE)
    }
}

private suspend fun setDocumentKeyInPoi(
    context: PipelineContext<Unit, ApplicationCall>,
    documentController: DocumentController,
    tripId: Int,
    poiId: Int,
    documentId: Int,
    poiType: PointOfInterestType
) {
    val key = context.receive<DocumentKeyRequest>(DocumentKeyRequest.MISSING_PARAM).key
    context.respond(documentController.setKey(context.getUserId(), tripId, poiId, documentId, key, poiType))
}