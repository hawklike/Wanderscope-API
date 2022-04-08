package cz.cvut.fit.steuejan.travel.data.database.place

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

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
            it[wikiBrief] = dto.wikiBrief
            it[imageUrl] = dto.imageUrl
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
        }?.value ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }

    override suspend fun find(tripId: Int, id: Int) = transaction {
        PlaceTable.selectFirst {
            (PlaceTable.trip eq tripId) and (PlaceTable.id eq id)
        }
    }?.let(PlaceDto::fromDb)

    override suspend fun edit(tripId: Int, poiId: Int, dto: PlaceDto) = transaction {
        PlaceTable.updateOrNull({
            (PlaceTable.id eq poiId) and (PlaceTable.trip eq tripId)
        }) {
            it[name] = dto.name
            it[type] = dto.type
            it[googlePlaceId] = dto.address.googlePlaceId
            it[address] = dto.address.address
            it[phone] = dto.contact.phone
            it[email] = dto.contact.email
            it[website] = dto.contact.website
            it[wikiBrief] = dto.wikiBrief
            it[imageUrl] = dto.imageUrl
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
        } ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }.isUpdated()

    override suspend fun delete(tripId: Int, poiId: Int) = transaction {
        PlaceTable.deleteWhere {
            (PlaceTable.id eq poiId) and (PlaceTable.trip eq tripId)
        }
    }.isDeleted()

    companion object {
        const val RESOURCE_NAME = "place"
    }
}