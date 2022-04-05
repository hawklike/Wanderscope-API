package cz.cvut.fit.steuejan.travel.api.user.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripDto

class UserController(private val daoFactory: DaoFactory) {

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
}