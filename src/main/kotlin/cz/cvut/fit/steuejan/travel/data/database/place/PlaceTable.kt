package cz.cvut.fit.steuejan.travel.data.database.place

import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.model.PlaceType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

object PlaceTable : IntIdTable("places") {
    val trip = reference("trip", TripTable)

    val name = varchar("name", 140)
    val type = enumerationByName("type", 10, PlaceType::class).default(PlaceType.OTHER)
    val googlePlaceId = text("google_place_id").nullable()
    val wikiBrief = text("wiki_brief").nullable()
    val imageUrl = text("image_url").nullable()
    val description = text("description").nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
}