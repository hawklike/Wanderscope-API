package cz.cvut.fit.steuejan.travel.api.trip.document.controller

import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.config.LimitsConfig
import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
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
import cz.cvut.fit.steuejan.travel.api.trip.document.model.FileWrapper
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType

class DocumentController(
    daoFactory: DaoFactory,
    private val validator: Validator,
    private val encryptor: Encryptor,
    private val limitsConfig: LimitsConfig
) : AbstractTripController(daoFactory) {

    suspend fun saveMetadata(
        userId: Int,
        tripId: Int,
        metadata: DocumentMetadata,
        poiId: Int? = null,
        poiType: PointOfInterestType? = null
    ): Response {
        editOrThrow(userId, tripId)

        poiType?.let {
            requireNotNull(poiId)
            getDao(it).find(tripId, poiId) ?: throw NotFoundException(it.notFound())
        }

        val hashedKey = metadata.key?.let {
            validator.validateDocumentKey(it)
            encryptor.hash(it)
        }
        val metadataHashedKey = metadata.copy(key = hashedKey)

        val id = daoFactory.documentDao.saveMetadata(userId, tripId, poiId, metadataHashedKey, poiType)
        return CreatedResponse.success(id)
    }

    suspend fun saveData(userId: Int, tripId: Int, documentId: Int, file: FileWrapper): Response {
        return saveData(userId, tripId, file) { fileWithExtension ->
            daoFactory.documentDao.saveData(tripId, documentId, fileWithExtension)
        }
    }

    suspend fun saveData(
        userId: Int,
        tripId: Int,
        poiId: Int,
        documentId: Int,
        file: FileWrapper,
        poiType: PointOfInterestType
    ): Response {
        return saveData(userId, tripId, file) { fileWithExtension ->
            daoFactory.documentDao.saveData(tripId, poiId, documentId, fileWithExtension, poiType)
        }
    }

    suspend fun getData(userId: Int, tripId: Int, documentId: Int, key: String?): FileWrapper {
        return getData(userId, tripId, key) {
            daoFactory.documentDao.getDocument(tripId, documentId)
        }
    }

    suspend fun getData(
        userId: Int,
        tripId: Int,
        poiId: Int,
        documentId: Int,
        key: String?,
        poiType: PointOfInterestType
    ): FileWrapper {
        return getData(userId, tripId, key) {
            daoFactory.documentDao.getDocument(tripId, poiId, documentId, poiType)
        }
    }

    private suspend fun getData(
        userId: Int,
        tripId: Int,
        key: String?,
        dbCall: suspend () -> DocumentDto?
    ): FileWrapper {
        viewOrThrow(userId, tripId)

        val document = dbCall.invoke() ?: throw NotFoundException(FailureMessages.DOCUMENT_NOT_FOUND)
        document.data ?: throw NotFoundException(FailureMessages.DOCUMENT_DATA_NULL)

        //document is secret
        document.key?.let { hashedKey ->
            if (key == null || hashedKey != encryptor.hash(key)) {
                throw ForbiddenException(FailureMessages.DOCUMENT_DATA_PROHIBITED)
            }
        }

        return FileWrapper(document.name, document.data)
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
            daoFactory.documentDao.setKey(tripId, poiId, documentId, hashedKey, poiType)
        }
    }

    private suspend fun setKey(
        userId: Int,
        tripId: Int,
        documentId: Int,
        key: String,
        dbCall: suspend (hashedKey: String) -> Boolean
    ): Response {
        editOrThrow(userId, tripId)

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
        return Success(Status.NO_CONTENT)
    }

    private suspend fun saveData(
        userId: Int,
        tripId: Int,
        file: FileWrapper,
        dbCall: suspend (file: FileWrapper) -> Boolean
    ): Response {
        editOrThrow(userId, tripId)

        if (file.rawData.size > limitsConfig.documentMaxSize) {
            throw BadRequestException(FailureMessages.documentMaxSize(limitsConfig.documentMaxSize))
        }

        val extension = validator.validateExtension(file.originalName)

        if (!dbCall.invoke(file.copy(extension = extension))) {
            throw NotFoundException(FailureMessages.DOCUMENT_NOT_FOUND)
        }

        return Success(Status.NO_CONTENT)
    }
}