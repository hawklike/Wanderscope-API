package cz.cvut.fit.steuejan.travel.data.dao.user

import cz.cvut.fit.steuejan.travel.data.dto.UserDto
import cz.cvut.fit.steuejan.travel.data.model.Username

class UserDaoImpl : UserDao {
    override fun addUser(username: Username, account: String, password: String?): UserDto {
        TODO("Not yet implemented")
    }

    override fun findByAccount(account: String): UserDto? {
        TODO("Not yet implemented")
    }

    override fun findByUsername(username: Username): UserDto? {
        TODO("Not yet implemented")
    }

    override fun changePassword(username: Username, newPassword: String): UserDto? {
        TODO("Not yet implemented")
    }
}