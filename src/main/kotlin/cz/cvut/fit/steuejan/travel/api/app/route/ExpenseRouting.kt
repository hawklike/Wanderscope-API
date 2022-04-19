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
import cz.cvut.fit.steuejan.travel.api.trip.expense.controller.ExpenseController
import cz.cvut.fit.steuejan.travel.api.trip.expense.controller.ExpenseRoomController
import cz.cvut.fit.steuejan.travel.api.trip.expense.request.ExpenseRequest
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
        val expenseController = controllerFactory.expenseController

        addExpenseRoom(expenseRoomController)
        showExpenseRoom(expenseRoomController)
        editExpenseRoom(expenseRoomController)
        deleteExpenseRoom(expenseRoomController)

        showExpenses(expenseRoomController)

        addExpense(expenseController)
        showExpense(expenseController)
        editExpense(expenseController)
        deleteExpense(expenseController)
    }
}

private fun Route.addExpenseRoom(expenseRoomController: ExpenseRoomController) {
    post<Trip.ExpenseRoom> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val room = receive<ExpenseRoomRequest>(ExpenseRoomRequest.MISSING_PARAM).toDto()
        respond(expenseRoomController.createRoom(getUserId(), tripId, room))
    }
}

private fun Route.addExpense(expenseController: ExpenseController) {
    post<Trip.ExpenseRoom.Expense> {
        val tripId = it.room.trip.id.throwIfMissing(it.room.trip::id.name)
        val roomId = it.room.expenseRoomId.throwIfMissing(it.room::expenseRoomId.name)
        val expense = receive<ExpenseRequest>(ExpenseRequest.MISSING_PARAM).toDto()
        respond(expenseController.addExpense(getUserId(), tripId, roomId, expense))
    }
}

private fun Route.showExpenseRoom(expenseRoomController: ExpenseRoomController) {
    get<Trip.ExpenseRoom> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val roomId = it.expenseRoomId.throwIfMissing(it::expenseRoomId.name)
        respond(expenseRoomController.showRoom(getUserId(), tripId, roomId))
    }
}

private fun Route.showExpenses(expenseRoomController: ExpenseRoomController) {
    get<Trip.ExpenseRoom.Expenses> {
        val tripId = it.room.trip.id.throwIfMissing(it.room.trip::id.name)
        val roomId = it.room.expenseRoomId.throwIfMissing(it.room::expenseRoomId.name)
        respond(expenseRoomController.showExpenses(getUserId(), tripId, roomId))
    }
}

private fun Route.showExpense(expenseController: ExpenseController) {
    get<Trip.ExpenseRoom.Expense> {
        val tripId = it.room.trip.id.throwIfMissing(it.room.trip::id.name)
        val roomId = it.room.expenseRoomId.throwIfMissing(it.room::expenseRoomId.name)
        val expenseId = it.expenseId.throwIfMissing(it::expenseId.name)
        respond(expenseController.showExpense(getUserId(), tripId, roomId, expenseId))
    }
}

private fun Route.editExpense(expenseController: ExpenseController) {
    put<Trip.ExpenseRoom.Expense> {
        val tripId = it.room.trip.id.throwIfMissing(it.room.trip::id.name)
        val roomId = it.room.expenseRoomId.throwIfMissing(it.room::expenseRoomId.name)
        val expenseId = it.expenseId.throwIfMissing(it::expenseId.name)
        val expense = receive<ExpenseRequest>(ExpenseRequest.MISSING_PARAM).toDto()
        respond(expenseController.editExpense(getUserId(), tripId, roomId, expenseId, expense))
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

private fun Route.deleteExpense(expenseController: ExpenseController) {
    delete<Trip.ExpenseRoom.Expense> {
        val tripId = it.room.trip.id.throwIfMissing(it.room.trip::id.name)
        val roomId = it.room.expenseRoomId.throwIfMissing(it.room::expenseRoomId.name)
        val expenseId = it.expenseId.throwIfMissing(it::expenseId.name)
        respond(expenseController.deleteExpense(getUserId(), tripId, roomId, expenseId))
    }
}


