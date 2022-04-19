package cz.cvut.fit.steuejan.travel.data.database.user.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripDto
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripOverviewDto
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserDto
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.model.Username
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.joda.time.DateTime

class UserDaoImpl : UserDao {

    override suspend fun addUser(
        username: Username,
        accountType: AccountType,
        email: String,
        password: String?
    ) = transaction {
        UserTable.insertAndGetIdOrNull {
            it[this.username] = username.it
            it[this.accountType] = accountType
            it[this.email] = email
            it[this.password] = password
        }?.value ?: throw BadRequestException(FailureMessages.ADD_USER_FAILURE)
    }

    override suspend fun findById(id: Int) = transaction {
        UserTable.findById(id)
    }?.let(UserDto::fromDb)

    override suspend fun findByEmail(email: String, accountType: AccountType) = transaction {
        UserTable.selectFirst { (UserTable.email eq email) and (UserTable.accountType eq accountType) }
    }?.let(UserDto::fromDb)

    override suspend fun findByUsername(username: Username) = transaction {
        UserTable.selectFirst { UserTable.username eq username.it }
    }?.let(UserDto::fromDb)

    override suspend fun deleteUser(userId: Int) = transaction {
        UserTable.updateById(userId) {
            it[deleted] = true
            it[displayName] = USER_DELETED
        }
    }.isUpdated()

    override suspend fun changePassword(userId: Int, newPassword: String) = transaction {
        UserTable.updateById(userId) { it[password] = newPassword }
    }.isUpdated()

    override suspend fun changeDisplayName(userId: Int, displayName: String) = transaction {
        UserTable.updateById(userId) { it[UserTable.displayName] = displayName }
    }.isUpdated()

    override suspend fun getAllTrips(userId: Int): List<TripOverviewDto> {
        val selection = UserTable.id eq userId
        return getTrips(selection, TripTable.startDate, SortOrder.DESC_NULLS_LAST)
    }

    override suspend fun getUpcomingTrips(userId: Int, localTime: DateTime): List<TripOverviewDto> {
        val selection = (UserTable.id eq userId) and (
                (TripTable.endDate.greaterEq(localTime)) or (TripTable.endDate.isNull()))
        return getTrips(selection, TripTable.startDate, SortOrder.ASC_NULLS_LAST)
    }

    override suspend fun getPastTrips(userId: Int, localTime: DateTime): List<TripOverviewDto> {
        val selection = (UserTable.id eq userId) and (TripTable.endDate.less(localTime))
        return getTrips(selection, TripTable.endDate, SortOrder.DESC_NULLS_LAST)
    }

    private suspend fun getTrips(
        selectWhere: Op<Boolean>,
        column: Expression<*>,
        orderBy: SortOrder
    ): List<TripOverviewDto> {
        val query = getTripsFieldSet()
            .select(selectWhere)
            .orderBy(column, orderBy)
            .withDistinct()

        return transaction {
            queryToTripsOverview(query)
        }
    }

    private fun getTripsFieldSet() = TripTable
        .innerJoin(TripUserTable)
        .join(UserTable, JoinType.INNER, TripUserTable.user, UserTable.id)
        .slice(TripTable.columns + TripUserTable.columns)

    private fun queryToTripsOverview(query: Query): List<TripOverviewDto> {
        val trips = mutableListOf<TripOverviewDto>()
        query.forEach {
            val trip = TripDto.fromDb(it)
            val connection = TripUserDto.fromDb(it)
            trips.add(TripOverviewDto(trip, connection))
        }
        return trips
    }

    companion object {
        const val USER_DELETED = "<deleted>"
    }
}
