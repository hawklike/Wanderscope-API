package cz.cvut.fit.steuejan.travel.data.database.tripuser.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.extension.insertOrNull
import cz.cvut.fit.steuejan.travel.data.extension.isDeleted
import cz.cvut.fit.steuejan.travel.data.extension.isUpdated
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.model.UserRole
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TripUserDaoImpl : TripUserDao {
    override suspend fun addConnection(userId: Int, tripId: Int, role: UserRole) {
        transaction {
            TripUserTable.insertOrNull {
                it[this.user] = userId
                it[this.trip] = tripId
                it[this.role] = role
            } ?: throw BadRequestException(FailureMessages.USER_OR_TRIP_NOT_FOUND)
        }
    }

    override suspend fun findConnection(userId: Int, tripId: Int) = transaction {
        TripUserTable.selectFirst { findById(userId, tripId) }
    }?.let { TripUserDto.fromDb(it) }

    override suspend fun removeConnection(userId: Int, tripId: Int) = transaction {
        TripUserTable.deleteWhere { findById(userId, tripId) }
    }.isDeleted()

    override suspend fun changeRole(userId: Int, tripId: Int, newRole: UserRole) = transaction {
        TripUserTable.update({ findById(userId, tripId) }) {
            it[role] = newRole
        }
    }.isUpdated()

    override suspend fun countUsersInTrip(tripId: Int) = transaction {
        TripUserTable.select { TripUserTable.trip eq tripId }.count()
    }

    override suspend fun countAdminsInTrip(tripId: Int) = transaction {
        TripUserTable.select { (TripUserTable.trip eq tripId) and (TripUserTable.role eq UserRole.ADMIN) }.count()
    }

    private fun findById(userId: Int, tripId: Int): Op<Boolean> {
        return (TripUserTable.user eq userId) and (TripUserTable.trip eq tripId)
    }
}