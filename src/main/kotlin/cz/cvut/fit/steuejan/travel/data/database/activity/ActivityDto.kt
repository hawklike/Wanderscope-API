package cz.cvut.fit.steuejan.travel.data.database.activity

import cz.cvut.fit.steuejan.travel.data.dto.Dto
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto
import cz.cvut.fit.steuejan.travel.data.model.ActivityType
import cz.cvut.fit.steuejan.travel.data.model.Address
import cz.cvut.fit.steuejan.travel.data.model.Coordinates
import cz.cvut.fit.steuejan.travel.data.model.Duration
import org.jetbrains.exposed.sql.ResultRow

data class ActivityDto(
    override val id: Int,
    override val tripId: Int,
    override val duration: Duration,
    override val name: String,
    val type: ActivityType?,
    val address: Address,
    val coordinates: Coordinates,
    val mapLink: String?,
    val description: String?
) : PointOfInterestDto, Dto() {
    companion object {
        fun fromDb(resultRow: ResultRow) = ActivityDto(
            id = resultRow[ActivityTable.id].value,
            tripId = resultRow[ActivityTable.trip].value,
            duration = Duration(
                startDate = resultRow[ActivityTable.startDate],
                endDate = resultRow[ActivityTable.endDate],
            ),
            name = resultRow[ActivityTable.name],
            type = resultRow[ActivityTable.type],
            address = Address(
                googlePlaceId = resultRow[ActivityTable.googlePlaceId],
                address = resultRow[ActivityTable.address]
            ),
            coordinates = Coordinates(
                longitude = resultRow[ActivityTable.longitude],
                latitude = resultRow[ActivityTable.latitude]
            ),
            mapLink = resultRow[ActivityTable.mapLink],
            description = resultRow[ActivityTable.description]
        )
    }
}
