package cz.cvut.fit.steuejan.travel.data.database.trip

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object TripTable : IntIdTable("trips") {
    val name = text("name")
    val startDate = datetime("start_date").default(DateTime.now())
}