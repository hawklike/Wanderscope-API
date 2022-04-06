package cz.cvut.fit.steuejan.travel.data.database.user.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserDto
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserDto
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import cz.cvut.fit.steuejan.travel.data.dto.TripOverviewDto
import cz.cvut.fit.steuejan.travel.data.extension.findById
import cz.cvut.fit.steuejan.travel.data.extension.insertAndGetIdOrNull
import cz.cvut.fit.steuejan.travel.data.extension.isUpdated
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.model.Username
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

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

    override suspend fun findById(id: Int) = UserTable.findById(id)?.let(UserDto::fromDb)

    override suspend fun findByEmail(email: String, accountType: AccountType) = transaction {
        UserTable.selectFirst { (UserTable.email eq email) and (UserTable.accountType eq accountType) }
    }?.let(UserDto::fromDb)

    override suspend fun findByUsername(username: Username) = transaction {
        UserTable.selectFirst { UserTable.username eq username.it }
    }?.let(UserDto::fromDb)

    override suspend fun changePassword(userId: Int, newPassword: String) = transaction {
        UserTable.update({ UserTable.id eq userId }) {
            it[password] = newPassword
        }
    }.isUpdated()

    override suspend fun getTrips(userId: Int): List<TripOverviewDto> {
        val query = TripTable
            .innerJoin(TripUserTable)
            .join(UserTable, JoinType.INNER, TripUserTable.user, UserTable.id)
            .slice(TripTable.columns + TripUserTable.columns)
            .select { UserTable.id eq userId }
            .withDistinct()

        return transaction {
            val zip = mutableListOf<TripOverviewDto>()
            query.forEach {
                val trip = TripDto.fromDb(it)
                val connection = TripUserDto.fromDb(it)
                zip.add(TripOverviewDto(trip, connection))
            }
            zip
        }
    }
}
