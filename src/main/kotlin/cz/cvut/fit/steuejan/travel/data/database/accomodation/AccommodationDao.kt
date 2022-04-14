package cz.cvut.fit.steuejan.travel.data.database.accomodation

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.model.AccommodationItinerary
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

@Suppress("DuplicatedCode")
class AccommodationDao : PointOfInterestDao<AccommodationDto> {
    override suspend fun add(tripId: Int, dto: AccommodationDto) = transaction {
        AccommodationTable.insertAndGetIdOrNull {
            it[trip] = tripId
            it[name] = dto.name
            it[type] = dto.type
            it[googlePlaceId] = dto.address.googlePlaceId
            it[address] = dto.address.address
            it[phone] = dto.contact.phone
            it[email] = dto.contact.email
            it[website] = dto.contact.website
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
        }?.value ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }

    override suspend fun find(tripId: Int, id: Int) = transaction {
        AccommodationTable.selectFirst { findById(tripId, id) }
    }?.let(AccommodationDto::fromDb)

    override suspend fun edit(tripId: Int, poiId: Int, dto: AccommodationDto) = transaction {
        AccommodationTable.updateOrNull({ findById(tripId, poiId) }) {
            it[name] = dto.name
            it[type] = dto.type
            it[googlePlaceId] = dto.address.googlePlaceId
            it[address] = dto.address.address
            it[phone] = dto.contact.phone
            it[email] = dto.contact.email
            it[website] = dto.contact.website
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
        } ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }.isUpdated()

    override suspend fun delete(tripId: Int, poiId: Int) = transaction {
        AccommodationTable.deleteWhere { findById(tripId, poiId) }
    }.isDeleted()

    override suspend fun show(tripId: Int) = transaction {
        AccommodationTable.select { AccommodationTable.trip eq tripId }
            .orderBy(AccommodationTable.startDate, SortOrder.ASC_NULLS_LAST)
            .map(AccommodationDto::fromDb)
    }

    override suspend fun showItinerary(tripId: Int) = transaction {
        AccommodationTable.select { AccommodationTable.trip eq tripId }
            .map(AccommodationDto::fromDb)
            .map(AccommodationItinerary::fromDto)
    }

    companion object {
        const val RESOURCE_NAME = "accommodation"

        private fun findById(tripId: Int, accommodationId: Int): Op<Boolean> {
            return (AccommodationTable.id eq accommodationId) and (AccommodationTable.trip eq tripId)
        }
    }
}