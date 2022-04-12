package cz.cvut.fit.steuejan.travel.api.trip.request

import cz.cvut.fit.steuejan.travel.api.app.request.Request
import cz.cvut.fit.steuejan.travel.api.trip.model.ChangeRole
import cz.cvut.fit.steuejan.travel.data.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class ChangeRoleRequest(
    val whomId: Int,
    val newRole: UserRole
) : Request {
    fun toModel() = ChangeRole(whomId, newRole)

    companion object {
        const val MISSING_PARAM = "Required 'whomId': Int, 'newRole': UserRole aka [ADMIN, EDITOR, VIEWER]."
    }
}
