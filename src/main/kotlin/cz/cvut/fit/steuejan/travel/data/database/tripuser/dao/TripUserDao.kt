package cz.cvut.fit.steuejan.travel.data.database.tripuser.dao

import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto
import cz.cvut.fit.steuejan.travel.data.model.UserRole

interface TripUserDao {
    suspend fun addConnection(userId: Int, tripId: Int, role: UserRole)
    suspend fun findConnection(userId: Int, tripId: Int): TripUserDto?
    suspend fun removeConnection(userId: Int, tripId: Int): Boolean
    suspend fun changeRole(userId: Int, tripId: Int, newRole: UserRole): Boolean
}