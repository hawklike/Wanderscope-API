package cz.cvut.fit.steuejan.travel.data.database.activity

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

@Suppress("DuplicatedCode")
class ActivityDao : PointOfInterestDao<ActivityDto> {
    override suspend fun add(tripId: Int, dto: ActivityDto) = transaction {
        ActivityTable.insertAndGetIdOrNull {
            it[trip] = tripId
            it[name] = dto.name
            it[type] = dto.type
            it[googlePlaceId] = dto.address.googlePlaceId
            it[address] = dto.address.address
            it[longitude] = dto.coordinates.longitude
            it[latitude] = dto.coordinates.latitude
            it[mapLink] = dto.mapLink
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
        }?.value ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }

    override suspend fun find(tripId: Int, id: Int) = transaction {
        ActivityTable.selectFirst {
            (ActivityTable.trip eq tripId) and (ActivityTable.id eq id)
        }
    }?.let(ActivityDto::fromDb)

    override suspend fun edit(tripId: Int, poiId: Int, dto: ActivityDto) = transaction {
        ActivityTable.updateOrNull({
            (ActivityTable.id eq poiId) and (ActivityTable.trip eq tripId)
        }) {
            it[name] = dto.name
            it[type] = dto.type
            it[googlePlaceId] = dto.address.googlePlaceId
            it[address] = dto.address.address
            it[longitude] = dto.coordinates.longitude
            it[latitude] = dto.coordinates.latitude
            it[mapLink] = dto.mapLink
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
        } ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }.isUpdated()

    override suspend fun delete(tripId: Int, poiId: Int) = transaction {
        ActivityTable.deleteWhere {
            (ActivityTable.id eq poiId) and (ActivityTable.trip eq tripId)
        }
    }.isDeleted()

    companion object {
        const val RESOURCE_NAME = "activity"
    }
}