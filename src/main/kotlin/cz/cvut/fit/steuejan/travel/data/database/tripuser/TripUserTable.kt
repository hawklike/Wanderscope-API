package cz.cvut.fit.steuejan.travel.data.database.tripuser

import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object TripUserTable : LongIdTable("trip_user") {
    val user = reference("user", UserTable, onDelete = CASCADE, onUpdate = CASCADE)
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE)
    val canEdit = bool("can_edit").default(true)

    init {
        index(true, user, trip)
    }
}