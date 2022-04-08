package cz.cvut.fit.steuejan.travel.data.database.trip.dao

import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto
import cz.cvut.fit.steuejan.travel.data.model.Duration

interface TripDao {
    suspend fun createTrip(ownerId: Int, canEdit: Boolean, trip: TripDto): Int
    suspend fun editTrip(tripId: Int, trip: TripDto): Boolean
    suspend fun findById(id: Int): TripDto?
    suspend fun deleteTrip(tripId: Int): Boolean
    suspend fun shareTrip()
    suspend fun editDate(tripId: Int, duration: Duration): Boolean
}