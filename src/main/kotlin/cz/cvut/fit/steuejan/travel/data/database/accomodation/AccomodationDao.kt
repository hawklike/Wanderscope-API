package cz.cvut.fit.steuejan.travel.data.database.accomodation

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction
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
        }?.value ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure("accomodation"))
    }

    override suspend fun find(tripId: Int, id: Int) = transaction {
        AccomodationTable.selectFirst {
            (AccomodationTable.trip eq tripId) and (AccomodationTable.id eq id)
        }
    }?.let(AccomodationDto::fromDb)

    override suspend fun edit(tripId: Int, poiId: Int, dto: AccomodationDto) = transaction {
        AccomodationTable.updateOrNull({
            (AccomodationTable.id eq poiId) and (AccomodationTable.trip eq tripId)
        }) {
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
        } ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure("accomodation"))
    }.isUpdated()

    override suspend fun delete(tripId: Int, poiId: Int) = transaction {
        AccomodationTable.deleteWhere {
            (AccomodationTable.id eq poiId) and (AccomodationTable.trip eq tripId)
        }
    }.isDeleted()
}