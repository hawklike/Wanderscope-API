package cz.cvut.fit.steuejan.travel.data.database.user.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.user.UserDto
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import cz.cvut.fit.steuejan.travel.data.extension.findById
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.model.Username
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertIgnoreAndGetId
import org.jetbrains.exposed.sql.update

class UserDaoImpl : UserDao {

    override suspend fun addUser(
        username: Username,
        accountType: AccountType,
        email: String,
        password: String?
    ): UserDto {
        val userId = transaction {
            UserTable.insertIgnoreAndGetId {
                it[this.username] = username.it
                it[this.accountType] = accountType
                it[this.email] = email
                it[this.password] = password
            } ?: throw BadRequestException(FailureMessages.ADD_USER_FAILURE)
        }

        return findById(userId.value)!!
    }

    override suspend fun findById(id: Int) = UserTable.findById(id)?.let {
        UserDto.fromDb(it)
    }

    override suspend fun findByEmail(email: String, accountType: AccountType) = transaction {
        UserTable.selectFirst { (UserTable.email eq email) and (UserTable.accountType eq accountType) }
    }?.let { UserDto.fromDb(it) }

    override suspend fun findByUsername(username: Username) = transaction {
        UserTable.selectFirst { UserTable.username eq username.it }
    }?.let { UserDto.fromDb(it) }

    override suspend fun changePassword(userId: Int, newPassword: String) = transaction {
        UserTable.update({ UserTable.id eq userId }) {
            it[password] = newPassword
        } == 1
    }
}