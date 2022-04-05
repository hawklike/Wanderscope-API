package cz.cvut.fit.steuejan.travel.data.database.trip.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.extension.findById
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.insertIgnoreAndGetId

class TripDaoImpl : TripDao {
    override suspend fun createTrip(
        name: String,
        ownerId: Int,
        duration: Duration,
        description: String?,
        imageUrl: String?
    ): TripDto {
        val tripId = transaction {
            TripTable.insertIgnoreAndGetId {
                it[this.name] = name
                it[this.owner] = ownerId
                it[this.startDate] = duration.startDate
                it[this.endDate] = duration.endDate
                it[this.description] = description
                it[this.imageUrl] = imageUrl
            } ?: throw BadRequestException(FailureMessages.ADD_TRIP_FAILURE)
        }

        return findById(tripId.value)!!
    }

    override suspend fun findById(id: Int) = TripTable.findById(id)?.let {
        TripDto.fromDb(it)
    }

    override suspend fun deleteTrip(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun shareTrip() {
        TODO("Not yet implemented")
    }
}