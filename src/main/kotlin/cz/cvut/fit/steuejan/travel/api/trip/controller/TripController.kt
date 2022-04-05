package cz.cvut.fit.steuejan.travel.api.trip.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto

class TripController(private val daoFactory: DaoFactory) {

    suspend fun createTrip(userId: Int, trip: TripDto): Response {
        if (trip.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }

        val tripId = with(trip) {
            daoFactory.tripDao.createTrip(name, userId, duration, description, imageUrl).id
        }

        daoFactory.tripUserDao.addConnection(userId, tripId, canEdit = true)
        return Success(Status.CREATED)
    }

    suspend fun deleteTrip(userId: Int, tripId: Int): Response {
        val trip = daoFactory.tripDao.findById(tripId)
            ?: throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)

        if (trip.ownerId != userId) {
            return Failure(Status.FORBIDDEN, FailureMessages.DELETE_TRIP_PROHIBITED)
        }

        return if (daoFactory.tripDao.deleteTrip(tripId)) {
            Success(Status.NO_CONTENT)
        } else {
            Failure(Status.BAD_REQUEST, FailureMessages.TRIP_NOT_FOUND)
        }
    }
}