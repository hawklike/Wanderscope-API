package cz.cvut.fit.steuejan.travel.data.database.trip.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripDto
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripUsersDto
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserDto
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.UserRole
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class TripDaoImpl : TripDao {

    override suspend fun createTrip(ownerId: Int, role: UserRole, trip: TripDto) = transaction {
        val tripId = TripTable.insertAndGetIdOrNull {
            it[name] = trip.name
            it[owner] = ownerId
            it[startDate] = trip.duration.startDate
            it[endDate] = trip.duration.endDate
            it[description] = trip.description
            it[imageUrl] = trip.imageUrl
        } ?: throw BadRequestException(FailureMessages.ADD_TRIP_FAILURE)

        TripUserTable.insertOrNull {
            it[this.user] = ownerId
            it[this.trip] = tripId
            it[this.role] = role
        } ?: throw BadRequestException(FailureMessages.USER_OR_TRIP_NOT_FOUND)

        return@transaction tripId.value
    }

    override suspend fun editTrip(tripId: Int, trip: TripDto) = transaction {
        TripTable.updateByIdOrNull(tripId) {
            it[name] = trip.name
            it[startDate] = trip.duration.startDate
            it[endDate] = trip.duration.endDate
            it[description] = trip.description
            it[imageUrl] = trip.imageUrl
        } ?: throw BadRequestException(FailureMessages.ADD_TRIP_FAILURE)
    }.isUpdated()

    override suspend fun findById(id: Int) = transaction {
        TripTable.findById(id)
    }?.let(TripDto::fromDb)

    override suspend fun deleteTrip(tripId: Int) = transaction {
        TripTable.deleteById(tripId)
    }.isDeleted()

    override suspend fun editDate(tripId: Int, duration: Duration) = transaction {
        TripTable.updateById(tripId) {
            it[startDate] = duration.startDate
            it[endDate] = duration.endDate
        }
    }.isUpdated()

    override suspend fun showUsers(tripId: Int, role: UserRole?): List<TripUsersDto> {
        val where = if (role != null) {
            (TripTable.id eq tripId) and (TripUserTable.role eq role)
        } else {
            TripTable.id eq tripId
        }

        val query = getUsersFieldSet()
            .select(where)
            .orderBy(TripUserTable.role)
            .withDistinct()

        return transaction {
            queryToTripUsers(query)
        }
    }

    override suspend fun shareTrip() {
        TODO("Not yet implemented")
    }

    private fun getUsersFieldSet() = TripTable
        .innerJoin(TripUserTable)
        .join(UserTable, JoinType.INNER, TripUserTable.user, UserTable.id)
        .slice(UserTable.columns + TripUserTable.columns)

    private fun queryToTripUsers(query: Query): List<TripUsersDto> {
        val users = mutableListOf<TripUsersDto>()
        query.forEach {
            val user = UserDto.fromDb(it)
            val connection = TripUserDto.fromDb(it)
            if (!user.deleted) {
                users.add(TripUsersDto(user, connection))
            }
        }
        return users
    }
}