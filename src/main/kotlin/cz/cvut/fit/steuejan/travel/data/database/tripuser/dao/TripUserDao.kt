package cz.cvut.fit.steuejan.travel.data.database.tripuser.dao

import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto

interface TripUserDao {
    suspend fun addConnection(userId: Int, tripId: Int, canEdit: Boolean)
    suspend fun findConnection(userId: Int, tripId: Int): TripUserDto?
}