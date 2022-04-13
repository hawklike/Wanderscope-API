package cz.cvut.fit.steuejan.travel.api.trip.model

import cz.cvut.fit.steuejan.travel.data.model.UserRole

data class ChangeRole(
    val whomId: Int,
    val newRole: UserRole?
)
