package cz.cvut.fit.steuejan.travel.data.database.forgotpassword

import cz.cvut.fit.steuejan.travel.data.dto.Dto
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class ForgotPasswordDto(
    val id: Int,
    val userId: Int,
    val token: String,
    val expiresAt: DateTime
) : Dto() {
    companion object {
        fun fromDb(resultRow: ResultRow) = ForgotPasswordDto(
            id = resultRow[ForgotPasswordTable.id].value,
            userId = resultRow[ForgotPasswordTable.user].value,
            token = resultRow[ForgotPasswordTable.token],
            expiresAt = resultRow[ForgotPasswordTable.expiresAt]
        )
    }
}
