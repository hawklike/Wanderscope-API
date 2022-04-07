package cz.cvut.fit.steuejan.travel.api.trip.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.ForbiddenException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages

abstract class AbstractTripController(protected val daoFactory: DaoFactory) {

    protected suspend fun canUserEdit(userId: Int, tripId: Int): Boolean {
        return daoFactory.tripUserDao.findConnection(userId, tripId)?.canEdit
            ?: throw ForbiddenException(FailureMessages.USER_TRIP_NOT_FOUND)
    }

    protected suspend fun editOrThrow(userId: Int, tripId: Int, edit: (suspend () -> Unit)? = null) {
        if (!canUserEdit(userId, tripId)) {
            throw ForbiddenException(FailureMessages.EDIT_TRIP_PROHIBITED)
        }
        edit?.invoke()
    }
}