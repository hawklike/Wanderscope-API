package cz.cvut.fit.steuejan.travel.data.dao.user

import cz.cvut.fit.steuejan.travel.data.database.InMemoryDatabase
import cz.cvut.fit.steuejan.travel.data.dto.UserDto
import cz.cvut.fit.steuejan.travel.data.model.Username

class UserDaoWithoutDbs(private val database: InMemoryDatabase) : UserDao {
    override fun addUser(username: Username, account: String, password: String?): UserDto {
        database.users.add(UserDto(username, account, password))
        return database.users.last()
    }

    override fun findByAccount(account: String): UserDto? {
        return database.users.find { it.account == account }
    }

    override fun findByUsername(username: Username): UserDto? {
        return database.users.find { it.username == username }
    }

    override fun changePassword(username: Username, newPassword: String): UserDto? {
        return findByUsername(username)?.apply { password = newPassword }
    }
}