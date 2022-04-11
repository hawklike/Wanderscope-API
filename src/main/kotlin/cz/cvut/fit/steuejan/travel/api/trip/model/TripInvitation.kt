package cz.cvut.fit.steuejan.travel.api.trip.model

import cz.cvut.fit.steuejan.travel.data.model.Username

data class TripInvitation(
    val username: Username,
    val tripId: Int,
    val canEdit: Boolean
)
