package cz.cvut.fit.steuejan.travel.api.trip.expense.request

import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseDto
import cz.cvut.fit.steuejan.travel.data.dto.Dto
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class ExpenseRequest(
    val name: String,
    val amountInCents: Long,
    val whoPaid: String,
    val whoOwes: List<String>,
    @Serializable(with = DateTimeSerializer::class)
    val date: DateTime?
) : Request {
    fun toDto() = ExpenseDto(
        id = Dto.UNKNOWN_ID.toLong(),
        tripId = Dto.UNKNOWN_ID,
        roomId = Dto.UNKNOWN_ID,
        name = name,
        amountInCents = amountInCents,
        whoPaid = whoPaid,
        whoOwes = whoOwes.toSet().toList(),
        date = date
    )

    companion object {
        const val MISSING_PARAM =
            "Required 'name': String, 'amountInCents': Long, 'whoPaid': String, 'whoOwes': Array<String>."
    }
}
