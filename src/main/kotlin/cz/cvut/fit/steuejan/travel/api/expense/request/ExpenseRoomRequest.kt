package cz.cvut.fit.steuejan.travel.api.expense.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseRoomDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseRoomRequest(
    val name: String,
    val currency: String,
    val persons: List<String> // persons names
) : Request {
    fun toDto() = ExpenseRoomDto(
        id = Dto.UNKNOWN_ID,
        tripId = Dto.UNKNOWN_ID,
        name = name,
        currency = currency,
        persons = persons
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, 'currency': String aka currency code, 'persons': Array<String>."
    }
}
