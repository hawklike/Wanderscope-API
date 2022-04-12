package cz.cvut.fit.steuejan.travel.data.database.trip.dao

import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripDto
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripUsersDto
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.UserRole

interface TripDao {
    suspend fun createTrip(ownerId: Int, role: UserRole, trip: TripDto): Int
    suspend fun editTrip(tripId: Int, trip: TripDto): Boolean
    suspend fun findById(id: Int): TripDto?
    suspend fun deleteTrip(tripId: Int): Boolean
    suspend fun shareTrip()
    suspend fun editDate(tripId: Int, duration: Duration): Boolean
    suspend fun showUsers(tripId: Int, role: UserRole?): List<TripUsersDto>
}