package cz.cvut.fit.steuejan.travel.api.trip.document.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentMetadata
import cz.cvut.fit.steuejan.travel.api.trip.document.response.DocumentMetadataResponse
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType

class DocumentController(daoFactory: DaoFactory) : AbstractTripController(daoFactory) {

    suspend fun saveMetadata(
        userId: Int,
        tripId: Int,
        poiId: Int,
        metadata: DocumentMetadata,
        poiType: PointOfInterestType
    ): Response {
        return editOrThrow(userId, tripId) {
            val id = daoFactory.documentDao.saveMetadata(userId, tripId, poiId, metadata, poiType)
            DocumentMetadataResponse.success(tripId, id)
        }
    }

//    suspend fun saveData(userId: Int, tripId: Int, document)
}