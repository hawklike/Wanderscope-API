package cz.cvut.fit.steuejan.travel.data.database.trip.dao

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.deleteWhere

class TripDaoImpl : TripDao {

    override suspend fun createTrip(
        name: String,
        ownerId: Int,
        duration: Duration,
        canEdit: Boolean,
        description: String?,
        imageUrl: String?
    ) = transaction {
        val tripId = TripTable.insertAndGetIdOrNull {
            it[this.name] = name
            it[this.owner] = ownerId
            it[this.startDate] = duration.startDate
            it[this.endDate] = duration.endDate
            it[this.description] = description
            it[this.imageUrl] = imageUrl
        } ?: throw BadRequestException(FailureMessages.ADD_TRIP_FAILURE)

        TripUserTable.insertOrNull {
            it[this.user] = ownerId
            it[this.trip] = tripId
            it[this.canEdit] = canEdit
        } ?: throw NotFoundException(FailureMessages.USER_OR_TRIP_NOT_FOUND)

        return@transaction tripId.value
    }

    override suspend fun editTrip(
        tripId: Int,
        name: String,
        duration: Duration,
        description: String?,
        imageUrl: String?
    ) = transaction {
        TripTable.updateByIdOrNull(tripId) {
            it[this.name] = name
            it[this.startDate] = duration.startDate
            it[this.endDate] = duration.endDate
            it[this.description] = description
            it[this.imageUrl] = imageUrl
        } ?: throw BadRequestException(FailureMessages.ADD_TRIP_FAILURE)
    }.isUpdated()

    override suspend fun findById(id: Int) = TripTable.findById(id)?.let(TripDto::fromDb)

    override suspend fun deleteTrip(tripId: Int) = transaction {
        TripTable.deleteWhere { TripTable.id eq tripId }
    }.isDeleted()

    override suspend fun shareTrip() {
        TODO("Not yet implemented")
    }
}