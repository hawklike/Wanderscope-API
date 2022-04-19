package cz.cvut.fit.steuejan.travel.data.database.user.dao

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripOverviewDto
import cz.cvut.fit.steuejan.travel.data.database.user.UserDto
import cz.cvut.fit.steuejan.travel.data.model.Username
import org.joda.time.DateTime

interface UserDao {
    suspend fun addUser(username: Username, accountType: AccountType, email: String, password: String?): Int
    suspend fun findById(id: Int): UserDto?
    suspend fun findByEmail(email: String, accountType: AccountType): UserDto?
    suspend fun findByUsername(username: Username): UserDto?
    suspend fun deleteUser(userId: Int): Boolean
    suspend fun changePassword(userId: Int, newPassword: String): Boolean
    suspend fun changeDisplayName(userId: Int, displayName: String): Boolean
    suspend fun getAllTrips(userId: Int): List<TripOverviewDto>
    suspend fun getUpcomingTrips(userId: Int, localTime: DateTime): List<TripOverviewDto>
    suspend fun getPastTrips(userId: Int, localTime: DateTime): List<TripOverviewDto>
}