package cz.cvut.fit.steuejan.travel.data.database.dao

import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto

interface PointOfInterestDao<in T : PointOfInterestDto> {
    suspend fun add(tripId: Int, dto: T)
    suspend fun delete(poiId: Int)
    suspend fun edit(tripId: Int, dto: T)
}