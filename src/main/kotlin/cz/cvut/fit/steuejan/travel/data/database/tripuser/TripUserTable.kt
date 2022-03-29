package cz.cvut.fit.steuejan.travel.data.database.tripuser

import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

object TripUserTable : IntIdTable("trip_user") {
    val user = reference("user", UserTable)
    val trip = reference("trip", TripTable)
    val canEdit = bool("can_edit").default(true)
}