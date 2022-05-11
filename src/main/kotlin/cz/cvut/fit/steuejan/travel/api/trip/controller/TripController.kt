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
import cz.cvut.fit.steuejan.travel.api.trip.exception.CannotChangeRoleException
import cz.cvut.fit.steuejan.travel.api.trip.exception.CannotChangeRoleToMyselfException
import cz.cvut.fit.steuejan.travel.api.trip.exception.CannotLeaveException
import cz.cvut.fit.steuejan.travel.api.trip.expense.response.ExpenseRoomOverviewListList
import cz.cvut.fit.steuejan.travel.api.trip.expense.response.ExpenseRoomResponse
import cz.cvut.fit.steuejan.travel.api.trip.model.ChangeRole
import cz.cvut.fit.steuejan.travel.api.trip.model.TripInvitation
import cz.cvut.fit.steuejan.travel.api.trip.model.TripUser
import cz.cvut.fit.steuejan.travel.api.trip.response.TripResponse
import cz.cvut.fit.steuejan.travel.api.trip.response.TripUsersResponse
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.dto.TripDto
import cz.cvut.fit.steuejan.travel.data.model.Duration
import cz.cvut.fit.steuejan.travel.data.model.UserRole

class TripController(daoFactory: DaoFactory) : AbstractTripController(daoFactory) {

    suspend fun createTrip(userId: Int, trip: TripDto): Response {
        validateNameLength(trip)
        val tripId = daoFactory.tripDao.createTrip(userId, UserRole.ADMIN, trip)
        return CreatedResponse.success(tripId)
    }

    suspend fun getTrip(userId: Int, tripId: Int): Response {
        val role = getUserRole(userId, tripId) //if user is not memeber of this trip, the method will throw
        val trip = daoFactory.tripDao.findById(tripId)
            ?: throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)
        return TripResponse.success(trip, role)
    }

    suspend fun deleteTrip(userId: Int, tripId: Int): Response {
        if (getUserRole(userId, tripId) != UserRole.ADMIN) {
            throw ForbiddenException(FailureMessages.DELETE_TRIP_PROHIBITED)
        }

        if (!daoFactory.tripDao.deleteTrip(tripId)) {
            throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)
        }

        return Success(Status.NO_CONTENT)
    }

    suspend fun editTrip(userId: Int, tripId: Int, trip: TripDto): Response {
        validateNameLength(trip)
        editOrThrow(userId, tripId)
        if (!daoFactory.tripDao.editTrip(tripId, trip)) {
            throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)
        }
        return Success(Status.NO_CONTENT)
    }

    suspend fun invite(userId: Int, invitation: TripInvitation): Response {
        val userRole = getUserRole(userId, invitation.tripId)

        if (!userRole.canEdit()) {
            throw ForbiddenException(FailureMessages.INVITE_PROHIBITED)
        }

        with(invitation) {
            val user = daoFactory.userDao.findByUsername(username)
                ?: throw NotFoundException(FailureMessages.USER_NOT_FOUND)

            if (user.deleted) {
                throw NotFoundException(FailureMessages.USER_NOT_FOUND)
            }

            //editor cannot invite user who would have then admin rights
            if (userRole == UserRole.EDITOR && role == UserRole.ADMIN) {
                throw ForbiddenException(FailureMessages.INVITE_EDITOR_PROHIBITED)
            }
            daoFactory.tripUserDao.addConnection(user.id, tripId, role)
        }

        return Success(Status.NO_CONTENT)
    }

    suspend fun showDocuments(userId: Int, tripId: Int): Response {
        viewOrThrow(userId, tripId)
        val documents = daoFactory.documentDao.getDocuments(tripId)
        return DocumentOverviewListResponse.success(documents.map(DocumentOverview::fromDto))
    }

    suspend fun changeDate(userId: Int, tripId: Int, duration: Duration): Response {
        editOrThrow(userId, tripId)
        if (!daoFactory.tripDao.editDate(tripId, duration)) {
            throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)
        }
        return Success(Status.NO_CONTENT)
    }

    suspend fun showUsers(userId: Int, tripId: Int, role: UserRole?): Response {
        viewOrThrow(userId, tripId)
        val dto = daoFactory.tripDao.showUsers(tripId, role)
        return TripUsersResponse.success(dto.map(TripUser::fromDto))
    }

    suspend fun showExpenseRooms(userId: Int, tripId: Int): Response {
        viewOrThrow(userId, tripId)
        val rooms = daoFactory.expenseRoomDao.showExpenseRooms(tripId)
        return ExpenseRoomOverviewListList.success(rooms.map(ExpenseRoomResponse::success))
    }

    suspend fun leave(userId: Int, tripId: Int): Response {
        val dao = daoFactory.tripUserDao

        val isAdmin = getUserRole(userId, tripId) == UserRole.ADMIN

        //user can leave trip if he is alone or there is another admin
        if (!isAdmin || dao.countUsersInTrip(tripId) == 1L || dao.countAdminsInTrip(tripId) >= 2L) {
            if (!daoFactory.tripUserDao.removeConnection(userId, tripId)) {
                throw ForbiddenException(FailureMessages.USER_TRIP_NOT_FOUND)
            }
        } else {
            throw CannotLeaveException()
        }

        return Success(Status.NO_CONTENT)
    }

    suspend fun changeRole(userId: Int, tripId: Int, changeRole: ChangeRole): Response {
        val dao = daoFactory.tripUserDao
        val userRole = getUserRole(userId, tripId)

        if (userRole != UserRole.ADMIN) {
            throw CannotChangeRoleException()
        }

        //remove user if no role set
        if (changeRole.newRole == null) {
            return leave(changeRole.whomId, tripId)
        }

        //at least one admin must stay in the trip
        if (dao.countAdminsInTrip(tripId) == 1L && userId == changeRole.whomId) {
            throw CannotChangeRoleToMyselfException()
        }

        if (!dao.changeRole(changeRole.whomId, tripId, changeRole.newRole)) {
            throw NotFoundException(FailureMessages.USER_TRIP_NOT_FOUND)
        }

        return Success(Status.NO_CONTENT)
    }

    private fun validateNameLength(trip: TripDto) {
        if (trip.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }
    }
}