package cz.cvut.fit.steuejan.travel.data.database.tripuser.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.extension.insertOrNull
import cz.cvut.fit.steuejan.travel.data.extension.isDeleted
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.model.UserRole
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

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

    private fun findById(userId: Int, tripId: Int): Op<Boolean> {
        return (TripUserTable.user eq userId) and (TripUserTable.trip eq tripId)
    }
}