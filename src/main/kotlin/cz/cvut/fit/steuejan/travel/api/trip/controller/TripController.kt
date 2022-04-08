package cz.cvut.fit.steuejan.travel.api.trip.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.ForbiddenException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Failure
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.model.TripInvitation
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto
import cz.cvut.fit.steuejan.travel.data.model.Duration

class TripController(daoFactory: DaoFactory) : AbstractTripController(daoFactory) {

    suspend fun createTrip(userId: Int, trip: TripDto): Response {
        if (trip.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }

        daoFactory.tripDao.createTrip(userId, true, trip)
        return Success(Status.CREATED)
    }

    suspend fun deleteTrip(userId: Int, tripId: Int): Response {
        val trip = daoFactory.tripDao.findById(tripId)
            ?: throw ForbiddenException(FailureMessages.USER_TRIP_NOT_FOUND)

        if (trip.ownerId != userId) {
            return Failure(Status.FORBIDDEN, FailureMessages.DELETE_TRIP_PROHIBITED)
        }

        if (!daoFactory.tripDao.deleteTrip(tripId)) {
            throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)
        }

        return Success(Status.NO_CONTENT)
    }

    suspend fun editTrip(userId: Int, tripId: Int, trip: TripDto): Response {
        if (trip.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }

        editOrThrow(userId, tripId) {
            if (!daoFactory.tripDao.editTrip(tripId, trip)) {
                throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)
            }
        }
        return Success(Status.NO_CONTENT)
    }

    suspend fun invite(userId: Int, invitation: TripInvitation): Response {
        editOrThrow<Unit>(userId, invitation.tripId)

        with(invitation) {
            val user = daoFactory.userDao.findByUsername(username)
                ?: throw NotFoundException(FailureMessages.USER_NOT_FOUND)
            daoFactory.tripUserDao.addConnection(user.id, tripId, canEdit)
        }

        return Success(Status.NO_CONTENT)
    }

    suspend fun changeDate(userId: Int, tripId: Int, duration: Duration): Response {
        editOrThrow(userId, tripId) {
            if (!daoFactory.tripDao.editDate(tripId, duration)) {
                throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)
            }
        }
        return Success(Status.NO_CONTENT)
    }
}