package cz.cvut.fit.steuejan.travel.data.database.tripuser

import org.jetbrains.exposed.sql.ResultRow

data class TripUserDto(
    val id: Long,
    val userId: Int,
    val tripId: Int,
    val canEdit: Boolean
) {
    companion object {
        fun fromDb(resultRow: ResultRow) = TripUserDto(
            id = resultRow[TripUserTable.id].value,
            userId = resultRow[TripUserTable.user].value,
            tripId = resultRow[TripUserTable.trip].value,
            canEdit = resultRow[TripUserTable.canEdit]
        )
    }
}
