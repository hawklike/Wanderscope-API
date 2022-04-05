package cz.cvut.fit.steuejan.travel.data.database.tripuser.dao

import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.insert

class TripUserDaoImpl : TripUserDao {
    override suspend fun addConnection(userId: Int, tripId: Int, canEdit: Boolean) {
        transaction {
            TripUserTable.insert {
                it[this.user] = userId
                it[this.trip] = tripId
                it[this.canEdit] = canEdit
            }
        }
    }
}