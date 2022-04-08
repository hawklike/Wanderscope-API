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
    suspend fun getDocument(poiId: Int, documentId: Int, poiType: PointOfInterestType): DocumentDto?
    suspend fun saveData(tripId: Int, documentId: Int, data: ByteArray): Boolean
    suspend fun saveData(poiId: Int, documentId: Int, data: ByteArray, poiType: PointOfInterestType): Boolean
    suspend fun setKey(tripId: Int, documentId: Int, key: String): Boolean
    suspend fun setKey(poiId: Int, documentId: Int, key: String, poiType: PointOfInterestType): Boolean
}