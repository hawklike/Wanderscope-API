package cz.cvut.fit.steuejan.travel.data.database.document

import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccomodationTable
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityTable
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceTable
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportTable
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object DocumentTable : IntIdTable("documents") {
    val owner = reference("owner", UserTable)
    val trip = reference("trip", TripTable)

    val transport = reference("transport", TransportTable).nullable()
    val place = reference("place", PlaceTable).nullable()
    val accomodation = reference("accomodation", AccomodationTable).nullable()
    val activity = reference("activity", ActivityTable).nullable()

    val name = varchar("name", 140)
    val created = datetime("created").default(DateTime.now())
    val extension = varchar("extension", 10)
    val key = char("key", 5).nullable()
    val data = blob("data").nullable()
}