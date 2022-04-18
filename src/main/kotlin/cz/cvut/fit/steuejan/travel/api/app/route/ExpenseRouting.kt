@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trip
import cz.cvut.fit.steuejan.travel.api.app.util.throwIfMissing
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig
import cz.cvut.fit.steuejan.travel.api.trip.expense.controller.ExpenseRoomController
import cz.cvut.fit.steuejan.travel.api.trip.expense.request.ExpenseRoomRequest
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.expenseRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWTConfig.JWT_AUTHENTICATION) {
        val expenseRoomController = controllerFactory.expenseRoomController

        addExpenseRoom(expenseRoomController)
        showExpenseRoom(expenseRoomController)
        editExpenseRoom(expenseRoomController)
        deleteExpenseRoom(expenseRoomController)
    }
}

private fun Route.addExpenseRoom(expenseRoomController: ExpenseRoomController) {
    post<Trip.ExpenseRoom> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val room = receive<ExpenseRoomRequest>(ExpenseRoomRequest.MISSING_PARAM).toDto()
        respond(expenseRoomController.createRoom(getUserId(), tripId, room))
    }
}

private fun Route.showExpenseRoom(expenseRoomController: ExpenseRoomController) {
    get<Trip.ExpenseRoom> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val roomId = it.expenseRoomId.throwIfMissing(it::expenseRoomId.name)
        respond(expenseRoomController.showRoom(getUserId(), tripId, roomId))
    }
}

private fun Route.editExpenseRoom(expenseRoomController: ExpenseRoomController) {
    put<Trip.ExpenseRoom> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val roomId = it.expenseRoomId.throwIfMissing(it::expenseRoomId.name)
        val room = receive<ExpenseRoomRequest>(ExpenseRoomRequest.MISSING_PARAM).toDto()
        respond(expenseRoomController.editRoom(getUserId(), tripId, roomId, room))
    }
}

private fun Route.deleteExpenseRoom(expenseRoomController: ExpenseRoomController) {
    delete<Trip.ExpenseRoom> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val roomId = it.expenseRoomId.throwIfMissing(it::expenseRoomId.name)
        respond(expenseRoomController.deleteRoom(getUserId(), tripId, roomId))
    }
}


