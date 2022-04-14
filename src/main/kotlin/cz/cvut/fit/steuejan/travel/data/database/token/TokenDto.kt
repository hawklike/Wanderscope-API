package cz.cvut.fit.steuejan.travel.data.database.token

import cz.cvut.fit.steuejan.travel.data.dto.Dto
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class TokenDto(
    val id: Long,
    val userId: Int,
    val refreshToken: String,
    val created: DateTime
) : Dto() {
    companion object {
        fun fromDb(resultRow: ResultRow) = TokenDto(
            id = resultRow[TokenTable.id].value,
            userId = resultRow[TokenTable.user].value,
            refreshToken = resultRow[TokenTable.refreshToken],
            created = resultRow[TokenTable.created]
        )
    }
}
