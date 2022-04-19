package cz.cvut.fit.steuejan.travel.api.trip.document.controller

import cz.cvut.fit.steuejan.travel.api.app.bussines.AmazonS3
import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.ForbiddenException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.*
import cz.cvut.fit.steuejan.travel.api.auth.util.Encryptor
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentMetadata
import cz.cvut.fit.steuejan.travel.api.trip.document.model.FileWrapper
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType
import cz.cvut.fit.steuejan.travel.data.model.UserRole

class DocumentController(
    daoFactory: DaoFactory,
    private val validator: Validator,
    private val encryptor: Encryptor,
    private val amazonS3: AmazonS3
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
        return saveData(userId, tripId, file) {
            daoFactory.documentDao.getDocument(tripId, documentId)
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
        return saveData(userId, tripId, file) {
            daoFactory.documentDao.getDocument(tripId, poiId, documentId, poiType)
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
        validateKey(document, key)
        return amazonS3.downloadFile(document.id.toString())
            ?: throw BadRequestException(FailureMessages.DOCUMENT_DATA_NULL)
    }

    private fun validateKey(document: DocumentDto, key: String?) {
        document.key?.let { hashedKey ->
            if (key == null || hashedKey != encryptor.hash(key)) {
                throw ForbiddenException(FailureMessages.DOCUMENT_DATA_PROHIBITED)
            }
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

    suspend fun deleteDocument(userId: Int, tripId: Int, documentId: Int): Response {
        return deleteDocument(userId, tripId, documentId) {
            daoFactory.documentDao.deleteDocument(tripId, documentId)
        }
    }

    suspend fun deleteDocument(
        userId: Int,
        tripId: Int,
        poiId: Int,
        documentId: Int,
        poiType: PointOfInterestType
    ): Response {
        return deleteDocument(userId, tripId, documentId) {
            daoFactory.documentDao.deleteDocument(tripId, poiId, documentId, poiType)
        }
    }

    private suspend fun deleteDocument(
        userId: Int,
        tripId: Int,
        documentId: Int,
        dbCall: suspend () -> Boolean
    ): Response {
        val document = daoFactory.documentDao.getDocument(tripId, documentId)
            ?: throw NotFoundException(FailureMessages.DOCUMENT_NOT_FOUND)

        //not admin nor owner of the document
        if (getUserRole(userId, tripId) != UserRole.ADMIN && document.userId != userId) {
            throw ForbiddenException(FailureMessages.DOCUMENT_DELETE_PROHIBITED)
        }

        if (amazonS3.deleteFile(documentId.toString())) {
            dbCall.invoke()
        } else {
            throw NotFoundException(FailureMessages.DOCUMENT_DELETE_FAILED)
        }

        return Success(Status.NO_CONTENT)
    }

    private suspend fun saveData(
        userId: Int,
        tripId: Int,
        file: FileWrapper,
        dbCall: suspend () -> DocumentDto?
    ): Response {
        editOrThrow(userId, tripId)
        validator.validateFileSize(file)
        validator.validateExtension(file.originalName)

        val document = dbCall.invoke() ?: throw NotFoundException(FailureMessages.DOCUMENT_NOT_FOUND)

        if (document.userId != userId) {
            throw ForbiddenException(FailureMessages.DOCUMENT_UPLOAD_PROHIBITED)
        }

        return if (amazonS3.uploadFile(document.id.toString(), document.name, file)) {
            daoFactory.documentDao.updateTime(document.id)
            Success(Status.NO_CONTENT)
        } else {
            Failure(Status.INTERNAL_ERROR, FailureMessages.DOCUMENT_UPLOAD_FAILED)
        }
    }
}