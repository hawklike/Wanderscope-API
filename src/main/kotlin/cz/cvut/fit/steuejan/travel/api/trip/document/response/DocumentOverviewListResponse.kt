package cz.cvut.fit.steuejan.travel.api.trip.document.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentOverview
import kotlinx.serialization.Serializable

@Serializable
data class DocumentOverviewListResponse(
    val documents: List<DocumentOverview>
) : Response by Success() {
    companion object {
        fun success(documents: List<DocumentOverview>) = DocumentOverviewListResponse(documents)
    }
}
