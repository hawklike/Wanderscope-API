package cz.cvut.fit.steuejan.travel.data.database.tripuser.dao

interface TripUserDao {
    suspend fun addConnection(userId: Int, tripId: Int, canEdit: Boolean)
}