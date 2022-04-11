package cz.cvut.fit.steuejan.travel.api.trip.document.response

import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import kotlinx.serialization.Serializable

@Serializable
data class DocumentOverviewListResponse(
    val name: String,
) : Response by Success()
