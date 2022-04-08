package cz.cvut.fit.steuejan.travel.data.database.accomodation

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

@Suppress("DuplicatedCode")
class AccomodationDao : PointOfInterestDao<AccomodationDto> {
    override suspend fun add(tripId: Int, dto: AccomodationDto) = transaction {
        AccomodationTable.insertAndGetIdOrNull {
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
        AccomodationTable.selectFirst { findById(tripId, id) }
    }?.let(AccomodationDto::fromDb)

    override suspend fun edit(tripId: Int, poiId: Int, dto: AccomodationDto) = transaction {
        AccomodationTable.updateOrNull({ findById(tripId, poiId) }) {
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
        AccomodationTable.deleteWhere { findById(tripId, poiId) }
    }.isDeleted()

    companion object {
        const val RESOURCE_NAME = "accomodation"

        private fun findById(tripId: Int, accommodationId: Int): Op<Boolean> {
            return ((AccomodationTable.id eq accommodationId) and (AccomodationTable.trip eq tripId))
        }
    }
}