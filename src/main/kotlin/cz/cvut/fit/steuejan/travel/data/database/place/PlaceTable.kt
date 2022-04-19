package cz.cvut.fit.steuejan.travel.data.database.place

import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.model.PlaceType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.jodatime.datetime

object PlaceTable : IntIdTable("places") {
    val trip = reference("trip", TripTable, onDelete = CASCADE, onUpdate = CASCADE)

    val name = varchar("name", DatabaseConfig.NAME_LENGTH)
    val type = enumerationByName("type", PlaceType.MAX_LENGTH, PlaceType::class).default(PlaceType.OTHER)
    val googlePlaceId = varchar("google_place_id", DatabaseConfig.TEXT_LENGTH).nullable()
    val address = varchar("address", DatabaseConfig.TEXT_LENGTH).nullable()
    val phone = varchar("phone", DatabaseConfig.PHONE_LENGTH).nullable()
    val email = varchar("email", DatabaseConfig.EMAIL_LENGTH).nullable()
    val website = varchar("website", DatabaseConfig.TEXT_LENGTH).nullable()
    val wikiBrief = varchar("wiki_brief", DatabaseConfig.WIKI_LENGTH).nullable()
    val imageUrl = varchar("image_url", DatabaseConfig.TEXT_LENGTH).nullable()
    val description = varchar("description", DatabaseConfig.DESCRIPTION_LENGTH).nullable()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
}