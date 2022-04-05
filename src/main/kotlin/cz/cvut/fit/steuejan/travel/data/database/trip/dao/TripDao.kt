package cz.cvut.fit.steuejan.travel.data.database.trip.dao

import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto
import cz.cvut.fit.steuejan.travel.data.model.Duration

interface TripDao {
    suspend fun createTrip(
        name: String,
        ownerId: Int,
        duration: Duration,
        description: String?,
        imageUrl: String?
    ): TripDto

    suspend fun findById(id: Int): TripDto?
    suspend fun deleteTrip(tripId: Int): Boolean
    suspend fun shareTrip()
}