package cz.cvut.fit.steuejan.travel.api.trip.expense.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.expense.model.SuggestedPayment
import kotlinx.serialization.Serializable

@Serializable
data class SuggestedPaymentsResponse(
    val payments: List<SuggestedPayment>
) : Response by Success() {
    companion object {
        fun success(payments: List<SuggestedPayment>) = SuggestedPaymentsResponse(payments)
    }
}
