package cz.cvut.fit.steuejan.travel.data.database.dao

import cz.cvut.fit.steuejan.travel.api.trip.itinerary.model.CommonItinerary
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto

interface PointOfInterestDao<T : PointOfInterestDto> {
    suspend fun add(tripId: Int, dto: T): Int
    suspend fun find(tripId: Int, id: Int): T?
    suspend fun edit(tripId: Int, poiId: Int, dto: T): Boolean
    suspend fun delete(tripId: Int, poiId: Int): Boolean
    suspend fun show(tripId: Int): List<T>
    suspend fun showItinerary(tripId: Int): List<CommonItinerary>
}