package cz.cvut.fit.steuejan.travel.data.database.trip

import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.model.Duration
import org.jetbrains.exposed.sql.ResultRow

data class TripDto(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val duration: Duration,
    val description: String?,
    val imageUrl: String?,
    val linkView: String?,
    val linkEdit: String?
) : Dto() {
    companion object {
        fun fromDb(resultRow: ResultRow) = TripDto(
            id = resultRow[TripTable.id].value,
            ownerId = resultRow[TripTable.owner].value,
            name = resultRow[TripTable.name],
            duration = Duration(
                startDate = resultRow[TripTable.startDate],
                endDate = resultRow[TripTable.endDate]
            ),
            description = resultRow[TripTable.description],
            imageUrl = resultRow[TripTable.imageUrl],
            linkView = resultRow[TripTable.linkView],
            linkEdit = resultRow[TripTable.linkEdit]
        )
    }
}
