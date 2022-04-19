package cz.cvut.fit.steuejan.travel.api.trip.expense.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseRoomDto
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseRoomResponse(
    val id: Int,
    val tripId: Int,
    val name: String,
    val currency: String,
    val persons: List<String>
) : Response by Success() {
    companion object {
        fun success(dto: ExpenseRoomDto) = with(dto) {
            ExpenseRoomResponse(id, tripId, name, currency, persons)
        }
    }
}
