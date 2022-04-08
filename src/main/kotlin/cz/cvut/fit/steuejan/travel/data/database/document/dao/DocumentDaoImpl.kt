package cz.cvut.fit.steuejan.travel.data.database.document.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.InternalServerErrorException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentMetadata
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentDto
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentTable
import cz.cvut.fit.steuejan.travel.data.extension.insertAndGetIdOrNull
import cz.cvut.fit.steuejan.travel.data.extension.isUpdated
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class DocumentDaoImpl : DocumentDao {
    override suspend fun saveMetadata(
        userId: Int,
        tripId: Int,
        poiId: Int,
        metadata: DocumentMetadata,
        poiType: PointOfInterestType?
    ): Int = transaction {

        val column = when (poiType) {
            PointOfInterestType.TRANSPORT -> DocumentTable.transport
            PointOfInterestType.ACCOMMODATION -> DocumentTable.accomodation
            PointOfInterestType.ACTIVITY -> DocumentTable.activity
            PointOfInterestType.PLACE -> DocumentTable.place
            else -> null
        }

        DocumentTable.insertAndGetIdOrNull {
            it[owner] = userId
            it[trip] = tripId
            column?.let { col -> it[col] = poiId }
            it[name] = metadata.name
            it[extension] = metadata.extension
            it[key] = metadata.key
            it[created] = DateTime.now(DateTimeZone.UTC)
        }?.value ?: throw BadRequestException(FailureMessages.ADD_DOCUMENT_METADATA_FAILURE)
    }

    override suspend fun getDocument(tripId: Int, documentId: Int) = transaction {
        DocumentTable.selectFirst { findById(tripId, documentId) }
    }?.let(DocumentDto::fromDb)

    override suspend fun saveData(tripId: Int, documentId: Int, data: ByteArray) = transaction {
        try {
            DocumentTable.update({ findById(tripId, documentId) }) {
                it[DocumentTable.data] = ExposedBlob(data)
            }
        } catch (ex: Exception) {
            throw InternalServerErrorException(FailureMessages.ADD_DOCUMENT_DATA_FAILURE + ex.message)
        }
    }.isUpdated()

    override suspend fun setKey(tripId: Int, documentId: Int, key: String) = transaction {
        DocumentTable.update({ findById(tripId, documentId) }) {
            it[DocumentTable.key] = key
        }
    }.isUpdated()

    companion object {
        fun findById(tripId: Int, documentId: Int): Op<Boolean> {
            return ((DocumentTable.id eq documentId) and (DocumentTable.trip eq tripId))
        }
    }
}