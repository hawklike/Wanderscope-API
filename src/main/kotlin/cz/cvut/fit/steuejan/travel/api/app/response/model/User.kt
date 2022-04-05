package cz.cvut.fit.steuejan.travel.api.app.response.model

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.user.UserDto

@kotlinx.serialization.Serializable
data class User(
    val username: String,
    val email: String,
    val accountType: AccountType
) {
    companion object {
        fun fromUserDto(userDto: UserDto) = with(userDto.credentials) {
            User(username.it, login.email, accountType)
        }
    }
}
