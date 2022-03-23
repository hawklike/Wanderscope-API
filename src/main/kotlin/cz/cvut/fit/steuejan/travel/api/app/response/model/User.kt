package cz.cvut.fit.steuejan.travel.api.app.response.model

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.dto.UserDto

@kotlinx.serialization.Serializable
data class User(
    val username: String,
    val email: String,
    val password: String?,
    val accountType: AccountType
) {
    companion object {
        fun fromUserDto(userDto: UserDto) = with(userDto) {
            User(
                username.it,
                account,
                password,
                if (password == null) AccountType.GOOGLE else AccountType.EMAIL
            )
        }
    }
}
