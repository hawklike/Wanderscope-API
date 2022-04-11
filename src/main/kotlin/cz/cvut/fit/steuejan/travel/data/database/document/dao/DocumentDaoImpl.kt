package cz.cvut.fit.steuejan.travel.data.database.document.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.InternalServerErrorException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentMetadata
import cz.cvut.fit.steuejan.travel.api.trip.document.model.FileWrapper
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentDto
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentTable
import cz.cvut.fit.steuejan.travel.data.extension.insertAndGetIdOrNull
import cz.cvut.fit.steuejan.travel.data.extension.isUpdated
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class DocumentDaoImpl : DocumentDao {
    override suspend fun saveMetadata(
        userId: Int,
        tripId: Int,
        poiId: Int?,
        metadata: DocumentMetadata,
        poiType: PointOfInterestType?
    ): Int = transaction {
        DocumentTable.insertAndGetIdOrNull {
            it[owner] = userId
            it[trip] = tripId
            selectColumn(poiType)?.let { col ->
                it[col] = poiId ?: throw BadRequestException(FailureMessages.ADD_DOCUMENT_METADATA_POI_NULL)
            }
            it[name] = metadata.name
            it[type] = metadata.type
            it[key] = metadata.key
            it[updated] = DateTime.now(DateTimeZone.UTC)
        }?.value ?: throw BadRequestException(FailureMessages.ADD_DOCUMENT_METADATA_FAILURE)
    }

    override suspend fun getDocument(tripId: Int, documentId: Int) = transaction {
        DocumentTable.selectFirst { findByIdInTrip(tripId, documentId) }
    }?.let(DocumentDto::fromDb)

    override suspend fun getDocument(tripId: Int, poiId: Int, documentId: Int, poiType: PointOfInterestType) =
        transaction {
            DocumentTable.selectFirst { findByIdInPoi(tripId, poiId, documentId, selectColumn(poiType)!!) }
        }?.let(DocumentDto::fromDb)

    override suspend fun getDocuments(tripId: Int) = transaction {
        DocumentTable.select { DocumentTable.trip eq tripId }.map(DocumentDto::fromDb)
    }

    override suspend fun getDocuments(tripId: Int, poiId: Int, poiType: PointOfInterestType) = transaction {
        DocumentTable.select {
            (DocumentTable.trip eq tripId) and (selectColumn(poiType)!! eq poiId)
        }.map(DocumentDto::fromDb)
    }

    override suspend fun saveData(tripId: Int, documentId: Int, data: FileWrapper): Boolean {
        return saveData(findByIdInTrip(tripId, documentId), data)
    }

    override suspend fun saveData(
        tripId: Int,
        poiId: Int,
        documentId: Int,
        data: FileWrapper,
        poiType: PointOfInterestType
    ): Boolean {
        return saveData(findByIdInPoi(tripId, poiId, documentId, selectColumn(poiType)!!), data)
    }

    override suspend fun setKey(tripId: Int, documentId: Int, key: String): Boolean {
        return setKey(findByIdInTrip(tripId, documentId), key)
    }

    override suspend fun setKey(
        tripId: Int,
        poiId: Int,
        documentId: Int,
        key: String,
        poiType: PointOfInterestType
    ): Boolean {
        return setKey(findByIdInPoi(tripId, poiId, documentId, selectColumn(poiType)!!), key)
    }

    private suspend fun setKey(updateWhere: Op<Boolean>, key: String) = transaction {
        DocumentTable.update({ updateWhere }) {
            it[DocumentTable.key] = key
        }
    }.isUpdated()

    private suspend fun saveData(updateWhere: Op<Boolean>, file: FileWrapper) = transaction {
        try {
            DocumentTable.update({ updateWhere }) {
                it[extension] = file.extension
                it[updated] = DateTime.now(DateTimeZone.UTC)
                it[data] = ExposedBlob(file.rawData)
            }
        } catch (ex: Exception) {
            throw InternalServerErrorException(FailureMessages.ADD_DOCUMENT_DATA_FAILURE + ex.message)
        }
    }.isUpdated()

    private fun selectColumn(poiType: PointOfInterestType?) = when (poiType) {
        PointOfInterestType.TRANSPORT -> DocumentTable.transport
        PointOfInterestType.ACCOMMODATION -> DocumentTable.accomodation
        PointOfInterestType.ACTIVITY -> DocumentTable.activity
        PointOfInterestType.PLACE -> DocumentTable.place
        else -> null
    }

    companion object {
        private fun findByIdInTrip(tripId: Int, documentId: Int): Op<Boolean> {
            return ((DocumentTable.id eq documentId) and (DocumentTable.trip eq tripId))
        }

        private fun findByIdInPoi(
            tripId: Int,
            poiId: Int,
            documentId: Int,
            poiColumn: Column<EntityID<Int>?>
        ): Op<Boolean> {
            return ((DocumentTable.id eq documentId) and (DocumentTable.trip eq tripId) and (poiColumn eq poiId))
        }
    }
}