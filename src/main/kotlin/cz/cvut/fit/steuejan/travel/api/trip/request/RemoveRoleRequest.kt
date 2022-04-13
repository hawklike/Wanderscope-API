package cz.cvut.fit.steuejan.travel.api.trip.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.trip.model.ChangeRole
import kotlinx.serialization.Serializable

@Serializable
data class RemoveRoleRequest(
    val whomId: Int
) : Request {
    fun toModel() = ChangeRole(whomId, null)

    companion object {
        const val MISSING_PARAM = "Required 'whomId': Int."
    }
}