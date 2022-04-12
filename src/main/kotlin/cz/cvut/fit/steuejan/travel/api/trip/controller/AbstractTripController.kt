package cz.cvut.fit.steuejan.travel.api.trip.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.ForbiddenException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType
import cz.cvut.fit.steuejan.travel.data.model.UserRole

@Suppress("MemberVisibilityCanBePrivate")
abstract class AbstractTripController(protected val daoFactory: DaoFactory) {

    protected suspend fun canUserEdit(userId: Int, tripId: Int): Boolean {
        return daoFactory.tripUserDao.findConnection(userId, tripId)?.role?.canEdit()
            ?: throw ForbiddenException(FailureMessages.USER_TRIP_NOT_FOUND)
    }

    protected suspend fun canUserView(userId: Int, tripId: Int): Boolean {
        return daoFactory.tripUserDao.findConnection(userId, tripId)?.role?.canView() ?: false
    }

    protected suspend fun getUserRole(userId: Int, tripId: Int): UserRole {
        return daoFactory.tripUserDao.findConnection(userId, tripId)?.role
            ?: throw ForbiddenException(FailureMessages.USER_TRIP_NOT_FOUND)
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

    protected fun getDao(poiType: PointOfInterestType): PointOfInterestDao<*> {
        return when (poiType) {
            PointOfInterestType.TRANSPORT -> daoFactory.transportDao
            PointOfInterestType.ACCOMMODATION -> daoFactory.accomodationDao
            PointOfInterestType.ACTIVITY -> daoFactory.activityDao
            PointOfInterestType.PLACE -> daoFactory.placeDao
        }
    }
}