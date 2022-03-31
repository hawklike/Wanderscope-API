package cz.cvut.fit.steuejan.travel.data.database.transport

import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.model.TransportType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.jodatime.datetime

object TransportTable : IntIdTable("transports") {
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE)

    val from = text("from")
    val to = text("to")
    val type = enumerationByName("type", 10, TransportType::class)
    val description = text("description").nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
    val cars = text("cars").nullable()
    val seats = text("seats").nullable()
}