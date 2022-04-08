package cz.cvut.fit.steuejan.travel.api.trip.document.controller

import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.ForbiddenException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.CreatedResponse
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.auth.util.Encryptor
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentMetadata
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType

class DocumentController(
    daoFactory: DaoFactory,
    private val validator: Validator,
    private val encryptor: Encryptor
) : AbstractTripController(daoFactory) {

    suspend fun saveMetadata(
        userId: Int,
        tripId: Int,
        metadata: DocumentMetadata,
        poiId: Int? = null,
        poiType: PointOfInterestType? = null
    ): Response {
        return editOrThrow(userId, tripId) {
            val hashedKey = metadata.key?.let {
                validator.validateDocumentKey(it)
                encryptor.hash(it)
            }
            val metadataHashedKey = metadata.copy(key = hashedKey)
            val id = daoFactory.documentDao.saveMetadata(userId, tripId, poiId, metadataHashedKey, poiType)
            CreatedResponse.success(id)
        }
    }

    suspend fun saveData(userId: Int, tripId: Int, documentId: Int, data: ByteArray): Response {
        return saveData(userId, tripId) {
            daoFactory.documentDao.saveData(tripId, documentId, data)
        }
    }

    suspend fun saveData(
        userId: Int,
        tripId: Int,
        poiId: Int,
        documentId: Int,
        data: ByteArray,
        poiType: PointOfInterestType
    ): Response {
        return saveData(userId, tripId) {
            daoFactory.documentDao.saveData(poiId, documentId, data, poiType)
        }
    }

    suspend fun setKey(userId: Int, tripId: Int, documentId: Int, key: String): Response {
        return setKey(userId, tripId, documentId, key) { hashedKey ->
            daoFactory.documentDao.setKey(tripId, documentId, hashedKey)
        }
    }

    suspend fun setKey(
        userId: Int,
        tripId: Int,
        poiId: Int,
        documentId: Int,
        key: String,
        poiType: PointOfInterestType
    ): Response {
        return setKey(userId, tripId, documentId, key) { hashedKey ->
            daoFactory.documentDao.setKey(poiId, documentId, hashedKey, poiType)
        }
    }

    private suspend fun setKey(
        userId: Int,
        tripId: Int,
        documentId: Int,
        key: String,
        dbCall: suspend (hashedKey: String) -> Boolean
    ): Response {
        return editOrThrow(userId, tripId) {
            val document = daoFactory.documentDao.getDocument(tripId, documentId)
                ?: throw NotFoundException(FailureMessages.DOCUMENT_NOT_FOUND)

            //key may set only owner
            if (document.userId != userId) {
                throw ForbiddenException(FailureMessages.DOCUMENT_SET_KEY_PROHIBITED)
            }

            validator.validateDocumentKey(key)
            val hashedKey = encryptor.hash(key)

            if (!dbCall.invoke(hashedKey)) {
                throw NotFoundException(FailureMessages.DOCUMENT_NOT_FOUND)
            }
            Success(Status.NO_CONTENT)
        }
    }

    private suspend fun saveData(userId: Int, tripId: Int, dbCall: suspend () -> Boolean): Response {
        return editOrThrow(userId, tripId) {
            if (!dbCall.invoke()) {
                throw NotFoundException(FailureMessages.DOCUMENT_NOT_FOUND)
            }
            Success(Status.NO_CONTENT)
        }
    }
}