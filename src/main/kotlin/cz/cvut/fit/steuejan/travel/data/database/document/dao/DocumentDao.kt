package cz.cvut.fit.steuejan.travel.data.database.document.dao

import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentMetadata
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType

interface DocumentDao {
    suspend fun saveMetadata(
        userId: Int,
        tripId: Int,
        poiId: Int?,
        metadata: DocumentMetadata,
        poiType: PointOfInterestType? = null
    ): Int

    suspend fun getDocument(tripId: Int, documentId: Int): DocumentDto?
    suspend fun getDocument(tripId: Int, poiId: Int, documentId: Int, poiType: PointOfInterestType): DocumentDto?

    suspend fun getDocuments(tripId: Int): List<DocumentDto>
    suspend fun getDocuments(tripId: Int, poiId: Int, poiType: PointOfInterestType): List<DocumentDto>

    suspend fun deleteDocument(tripId: Int, documentId: Int): Boolean
    suspend fun deleteDocument(tripId: Int, poiId: Int, documentId: Int, poiType: PointOfInterestType): Boolean

    suspend fun setKey(tripId: Int, documentId: Int, key: String): Boolean
    suspend fun setKey(tripId: Int, poiId: Int, documentId: Int, key: String, poiType: PointOfInterestType): Boolean

    suspend fun updateTime(documentId: Int): Boolean
}