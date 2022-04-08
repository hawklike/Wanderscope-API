package cz.cvut.fit.steuejan.travel.api.trip.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.ForbiddenException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages

@Suppress("MemberVisibilityCanBePrivate")
abstract class AbstractTripController(protected val daoFactory: DaoFactory) {

    protected suspend fun canUserEdit(userId: Int, tripId: Int): Boolean {
        return daoFactory.tripUserDao.findConnection(userId, tripId)?.canEdit
            ?: throw ForbiddenException(FailureMessages.USER_TRIP_NOT_FOUND)
    }

    protected suspend fun canUserView(userId: Int, tripId: Int): Boolean {
        return daoFactory.tripUserDao.findConnection(userId, tripId) != null
    }

    protected suspend fun <T> editOrThrow(userId: Int, tripId: Int, edit: suspend () -> T): T {
        if (!canUserEdit(userId, tripId)) {
            throw ForbiddenException(FailureMessages.EDIT_TRIP_PROHIBITED)
        }
        return edit.invoke()
    }

    protected suspend fun editOrThrow(userId: Int, tripId: Int) {
        editOrThrow(userId, tripId) {}
    }

    protected suspend fun <T> viewOrThrow(userId: Int, tripId: Int, view: suspend () -> T): T {
        if (!canUserView(userId, tripId)) {
            throw ForbiddenException(FailureMessages.USER_TRIP_NOT_FOUND)
        }
        return view.invoke()
    }

    protected suspend fun viewOrThrow(userId: Int, tripId: Int) {
        viewOrThrow(userId, tripId) {}
    }
}