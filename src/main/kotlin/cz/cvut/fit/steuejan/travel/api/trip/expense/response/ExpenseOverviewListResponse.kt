package cz.cvut.fit.steuejan.travel.api.trip.expense.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.expense.model.ExpenseOverview
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseOverviewListResponse(
    val expenses: List<ExpenseOverview>
) : Response by Success() {
    companion object {
        fun success(expenses: List<ExpenseOverview>) = ExpenseOverviewListResponse(expenses)
    }
}
