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

class TripDaoImpl : TripDao {

    override suspend fun createTrip(ownerId: Int, canEdit: Boolean, trip: TripDto) = transaction {
        val tripId = TripTable.insertAndGetIdOrNull {
            it[name] = trip.name
            it[owner] = ownerId
            it[startDate] = trip.duration.startDate
            it[endDate] = trip.duration.endDate
            it[description] = trip.description
            it[imageUrl] = trip.imageUrl
        } ?: throw BadRequestException(FailureMessages.ADD_TRIP_FAILURE)

        TripUserTable.insertOrNull {
            it[this.user] = ownerId
            it[this.trip] = tripId
            it[this.canEdit] = canEdit
        } ?: throw NotFoundException(FailureMessages.USER_OR_TRIP_NOT_FOUND)

        return@transaction tripId.value
    }

    override suspend fun editTrip(tripId: Int, trip: TripDto) = transaction {
        TripTable.updateByIdOrNull(tripId) {
            it[name] = trip.name
            it[startDate] = trip.duration.startDate
            it[endDate] = trip.duration.endDate
            it[description] = trip.description
            it[imageUrl] = trip.imageUrl
        } ?: throw BadRequestException(FailureMessages.ADD_TRIP_FAILURE)
    }.isUpdated()

    override suspend fun findById(id: Int) = transaction {
        TripTable.findById(id)
    }?.let(TripDto::fromDb)

    override suspend fun deleteTrip(tripId: Int) = transaction {
        TripTable.deleteById(tripId)
    }.isDeleted()

    override suspend fun editDate(tripId: Int, duration: Duration) = transaction {
        TripTable.updateById(tripId) {
            it[startDate] = duration.startDate
            it[endDate] = duration.endDate
        }
    }.isUpdated()

    override suspend fun shareTrip() {
        TODO("Not yet implemented")
    }
}