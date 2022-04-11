package cz.cvut.fit.steuejan.travel.data.database.user

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.Credentials
import cz.cvut.fit.steuejan.travel.data.model.EmailLogin
import cz.cvut.fit.steuejan.travel.data.model.GoogleLogin
import cz.cvut.fit.steuejan.travel.data.model.Username
import org.jetbrains.exposed.sql.ResultRow

data class UserDto(
    val id: Int,
    val credentials: Credentials<*>,
    val displayName: String?,
    val deleted: Boolean
) : Dto() {
    companion object {
        fun fromDb(resultRow: ResultRow): UserDto {
            val username = Username(resultRow[UserTable.username])
            val email = resultRow[UserTable.email]

            val credentials = when (resultRow[UserTable.accountType]) {
                AccountType.EMAIL -> Credentials(
                    username = username,
                    login = EmailLogin(email, resultRow[UserTable.password]!!)
                )
                AccountType.GOOGLE -> Credentials(
                    username = username,
                    login = GoogleLogin(email)
                )
            }

            return UserDto(
                id = resultRow[UserTable.id].value,
                credentials = credentials,
                displayName = resultRow[UserTable.displayName],
                deleted = resultRow[UserTable.deleted]
            )
        }
    }
}
