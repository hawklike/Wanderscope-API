package cz.cvut.fit.steuejan.travel.data.database.user.dao

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.user.UserDto
import cz.cvut.fit.steuejan.travel.data.model.Username

interface UserDao {
    suspend fun addUser(username: Username, accountType: AccountType, email: String, password: String?): Int
    suspend fun findById(id: Int): UserDto?
    suspend fun findByEmail(email: String, accountType: AccountType): UserDto?
    suspend fun findByUsername(username: Username): UserDto?
    suspend fun changePassword(userId: Int, newPassword: String): Boolean
}