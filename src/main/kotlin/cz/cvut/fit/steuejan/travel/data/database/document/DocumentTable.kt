package cz.cvut.fit.steuejan.travel.data.database.document

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccommodationTable
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityTable
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceTable
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportTable
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import cz.cvut.fit.steuejan.travel.data.model.DocumentType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.ReferenceOption.SET_NULL
import org.jetbrains.exposed.sql.jodatime.datetime

object DocumentTable : IntIdTable("documents") {
    val owner = reference("owner", UserTable, onDelete = SET_NULL, onUpdate = CASCADE)
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE)

    val transport = reference("transport", TransportTable, onDelete = CASCADE, onUpdate = CASCADE).nullable()
    val place = reference("place", PlaceTable, onDelete = CASCADE, onUpdate = CASCADE).nullable()
    val accomodation = reference("accomodation", AccommodationTable, onDelete = CASCADE, onUpdate = CASCADE).nullable()
    val activity = reference("activity", ActivityTable, onDelete = CASCADE, onUpdate = CASCADE).nullable()

    val name = varchar("name", DatabaseConfig.NAME_LENGTH)
    val updated = datetime("updated")
    val type = enumerationByName("type", DocumentType.MAX_LENGTH, DocumentType::class)
    val key = text("key").nullable() //hashed with HmacSHA256
}