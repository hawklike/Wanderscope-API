package cz.cvut.fit.steuejan.travel.api.trip.expense.model

import cz.cvut.fit.steuejan.travel.api.trip.expense.bussiness.SimplifyDebts
import kotlinx.serialization.Serializable

@Serializable
data class SuggestedPayment(
    val from: String,
    val to: String,
    val amountInCents: Long
) {
    companion object {
        fun create(suggestedPayment: SimplifyDebts.SuggestedPayment) = with(suggestedPayment) {
            SuggestedPayment(from, to, amountInCents)
        }
    }
}
