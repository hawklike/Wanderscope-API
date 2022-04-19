package cz.cvut.fit.steuejan.travel.api.trip.expense.model

import cz.cvut.fit.steuejan.travel.api.app.plugin.DateTimeSerializer
import cz.cvut.fit.steuejan.travel.data.database.expense.dto.ExpenseDto
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class ExpenseOverview(
    val id: Long,
    val name: String,
    val whoPaid: String,
    val amountInCents: Long,
    @Serializable(with = DateTimeSerializer::class)
    val date: DateTime?
) {
    companion object {
        fun fromDto(dto: ExpenseDto) = with(dto) {
            ExpenseOverview(id, name, whoPaid, amountInCents, date)
        }
    }
}
