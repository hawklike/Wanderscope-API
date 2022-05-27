package cz.cvut.fit.steuejan.travel.api.user.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.user.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val username: String,
    val email: String,
    val accountType: AccountType,
    val displayName: String?
) : Response by Success() {
    companion object {
        fun success(dto: UserDto) = UserResponse(
            dto.id,
            dto.credentials.username.it,
            dto.credentials.login.email,
            dto.credentials.accountType,
            dto.displayName
        )
    }
}
