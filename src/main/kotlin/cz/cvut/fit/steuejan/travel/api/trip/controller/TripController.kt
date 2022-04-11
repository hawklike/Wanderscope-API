package cz.cvut.fit.steuejan.travel.api.trip.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.ForbiddenException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.CreatedResponse
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentOverview
import cz.cvut.fit.steuejan.travel.api.trip.document.response.DocumentOverviewListResponse
import cz.cvut.fit.steuejan.travel.api.trip.model.TripInvitation
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto
import cz.cvut.fit.steuejan.travel.data.model.Duration

class TripController(daoFactory: DaoFactory) : AbstractTripController(daoFactory) {

    suspend fun createTrip(userId: Int, trip: TripDto): Response {
        if (trip.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }

        val tripId = daoFactory.tripDao.createTrip(userId, true, trip)
        return CreatedResponse.success(tripId)
    }

    suspend fun deleteTrip(userId: Int, tripId: Int): Response {
        val trip = daoFactory.tripDao.findById(tripId)
            ?: throw ForbiddenException(FailureMessages.USER_TRIP_NOT_FOUND)

        if (trip.ownerId != userId) {
            throw ForbiddenException(FailureMessages.DELETE_TRIP_PROHIBITED)
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
        editOrThrow(userId, invitation.tripId)

        with(invitation) {
            val user = daoFactory.userDao.findByUsername(username)
                ?: throw NotFoundException(FailureMessages.USER_NOT_FOUND)
            daoFactory.tripUserDao.addConnection(user.id, tripId, canEdit)
        }

        return Success(Status.NO_CONTENT)
    }

    suspend fun showDocuments(userId: Int, tripId: Int): Response {
        viewOrThrow(userId, tripId)
        val documents = daoFactory.documentDao.getDocuments(tripId)
        return DocumentOverviewListResponse.success(documents.map(DocumentOverview::fromDto))
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