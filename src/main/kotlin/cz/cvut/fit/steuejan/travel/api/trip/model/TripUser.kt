package cz.cvut.fit.steuejan.travel.api.trip.model

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripUsersDto
import cz.cvut.fit.steuejan.travel.data.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class TripUser(
    val id: Int,
    val username: String,
    val displayName: String?,
    val email: String,
    val accountType: AccountType,
    val role: UserRole
) {
    companion object {
        fun fromDto(dto: TripUsersDto) = with(dto.user) {
            TripUser(
                id = id,
                username = credentials.username.it,
                displayName = displayName,
                email = credentials.login.email,
                accountType = credentials.accountType,
                role = dto.connection.role
            )
        }
    }
}
