package cz.cvut.fit.steuejan.travel.data.database.place

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.model.PlaceItinerary
import cz.cvut.fit.steuejan.travel.api.trip.model.Language
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

@Suppress("DuplicatedCode")
class PlaceDao : PointOfInterestDao<PlaceDto> {
    override suspend fun add(tripId: Int, dto: PlaceDto) = transaction {
        PlaceTable.insertAndGetIdOrNull {
            it[trip] = tripId
            it[name] = dto.name
            it[type] = dto.type
            it[googlePlaceId] = dto.address.googlePlaceId
            it[address] = dto.address.address
            it[phone] = dto.contact.phone
            it[email] = dto.contact.email
            it[website] = dto.contact.website
            it[imageUrl] = dto.imageUrl
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
            it[longitude] = dto.coordinates.longitude
            it[latitude] = dto.coordinates.latitude
        }?.value ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }

    override suspend fun find(tripId: Int, id: Int) = transaction {
        PlaceTable.selectFirst { findById(tripId, id) }
    }?.let(PlaceDto::fromDb)

    override suspend fun edit(tripId: Int, poiId: Int, dto: PlaceDto) = transaction {
        PlaceTable.updateOrNull({ findById(tripId, poiId) }) {
            it[name] = dto.name
            it[type] = dto.type
            it[googlePlaceId] = dto.address.googlePlaceId
            it[address] = dto.address.address
            it[phone] = dto.contact.phone
            it[email] = dto.contact.email
            it[website] = dto.contact.website
            it[imageUrl] = dto.imageUrl
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
            it[longitude] = dto.coordinates.longitude
            it[latitude] = dto.coordinates.latitude
        } ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }.isUpdated()

    override suspend fun delete(tripId: Int, poiId: Int) = transaction {
        PlaceTable.deleteWhere { findById(tripId, poiId) }
    }.isDeleted()

    override suspend fun show(tripId: Int) = transaction {
        PlaceTable.select { PlaceTable.trip eq tripId }
            .orderBy(PlaceTable.startDate, SortOrder.ASC_NULLS_LAST)
            .map(PlaceDto::fromDb)
    }

    override suspend fun showItinerary(tripId: Int) = transaction {
        PlaceTable.select { PlaceTable.trip eq tripId }
            .map(PlaceDto::fromDb)
            .map(PlaceItinerary::fromDto)
    }

    suspend fun updateWiki(tripId: Int, poiId: Int, language: Language, wikiBrief: String) = transaction {
        PlaceTable.updateOrNull({ findById(tripId, poiId) }) {
            when (language) {
                Language.CS -> it[this.wikiBriefCzech] = truncateWiki(wikiBrief)
                Language.EN -> it[this.wikiBrief] = truncateWiki(wikiBrief)
            }
        }?.isUpdated()
    }

    private fun findById(tripId: Int, placeId: Int): Op<Boolean> {
        return (PlaceTable.id eq placeId) and (PlaceTable.trip eq tripId)
    }

    private fun truncateWiki(wikiBrief: String?): String? {
        val truncated = wikiBrief?.take(cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig.WIKI_LENGTH)
        return if ((truncated?.length ?: 0) < (wikiBrief?.length ?: 0)) {
            truncated?.plus("???")
        } else {
            truncated
        }
    }

    companion object {
        const val RESOURCE_NAME = "place"
    }
}