package cz.cvut.fit.steuejan.travel.data.dao.user

import cz.cvut.fit.steuejan.travel.data.dto.UserDto
import cz.cvut.fit.steuejan.travel.data.model.Username

interface UserDao {
    fun addUser(username: Username, account: String, password: String?): UserDto
    fun findByAccount(account: String): UserDto?
    fun findByUsername(username: Username): UserDto?
    fun changePassword(username: Username, newPassword: String): UserDto?
}