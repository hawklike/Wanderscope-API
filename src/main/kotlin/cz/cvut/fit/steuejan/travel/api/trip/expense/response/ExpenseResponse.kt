package cz.cvut.fit.steuejan.travel.api.trip.expense.response

import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseDto
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class ExpenseResponse(
    val id: Long,
    val tripId: Int,
    val roomId: Int,
    val name: String,
    val amountInCents: Long,
    val whoPaid: String,
    val whoOwes: List<String>,
    @Serializable(with = DateTimeSerializer::class)
    val date: DateTime?
) : Response by Success() {
    companion object {
        fun success(dto: ExpenseDto) = with(dto) {
            ExpenseResponse(id, tripId, roomId, name, amountInCents, whoPaid, whoOwes, date)
        }
    }
}