package cz.cvut.fit.steuejan.travel.api.trip.expense.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseRoomOverviewListList(
    val rooms: List<ExpenseRoomResponse>
) : Response by Success() {
    companion object {
        fun success(rooms: List<ExpenseRoomResponse>) = ExpenseRoomOverviewListList(rooms)
    }
}