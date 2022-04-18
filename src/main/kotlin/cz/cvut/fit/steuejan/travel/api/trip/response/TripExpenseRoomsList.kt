package cz.cvut.fit.steuejan.travel.api.trip.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.expense.response.ExpenseRoomResponse
import kotlinx.serialization.Serializable

@Serializable
data class TripExpenseRoomsList(
    val rooms: List<ExpenseRoomResponse>
) : Response by Success() {
    companion object {
        fun success(rooms: List<ExpenseRoomResponse>) = TripExpenseRoomsList(rooms)
    }
}