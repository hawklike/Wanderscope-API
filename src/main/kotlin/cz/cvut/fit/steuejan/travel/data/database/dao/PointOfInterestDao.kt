package cz.cvut.fit.steuejan.travel.data.database.dao

import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto

interface PointOfInterestDao<in T : PointOfInterestDto> {
    suspend fun add(tripId: Int, dto: T): Int
    suspend fun delete(poiId: Int): Boolean
    suspend fun edit(poiId: Int, dto: T): Boolean
}