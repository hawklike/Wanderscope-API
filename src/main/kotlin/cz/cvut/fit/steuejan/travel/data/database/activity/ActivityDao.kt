package cz.cvut.fit.steuejan.travel.data.database.activity

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.model.ActivityItinerary
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

@Suppress("DuplicatedCode")
class ActivityDao : PointOfInterestDao<ActivityDto> {
    override suspend fun add(tripId: Int, dto: ActivityDto) = transaction {
        ActivityTable.insertAndGetIdOrNull {
            it[trip] = tripId
            it[name] = dto.name
            it[type] = dto.type
            it[googlePlaceId] = dto.address.googlePlaceId
            it[address] = dto.address.address
            it[mapLink] = dto.mapLink
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
        }?.value ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }

    override suspend fun find(tripId: Int, id: Int) = transaction {
        ActivityTable.selectFirst { findById(tripId, id) }
    }?.let(ActivityDto::fromDb)

    override suspend fun edit(tripId: Int, poiId: Int, dto: ActivityDto) = transaction {
        ActivityTable.updateOrNull({ findById(tripId, poiId) }) {
            it[name] = dto.name
            it[type] = dto.type
            it[googlePlaceId] = dto.address.googlePlaceId
            it[address] = dto.address.address
            it[mapLink] = dto.mapLink
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
        } ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }.isUpdated()

    override suspend fun delete(tripId: Int, poiId: Int) = transaction {
        ActivityTable.deleteWhere { findById(tripId, poiId) }
    }.isDeleted()

    override suspend fun show(tripId: Int) = transaction {
        ActivityTable.select { ActivityTable.trip eq tripId }
            .orderBy(ActivityTable.startDate, SortOrder.ASC_NULLS_LAST)
            .map(ActivityDto::fromDb)
    }

    override suspend fun showItinerary(tripId: Int) = transaction {
        ActivityTable.select { ActivityTable.trip eq tripId }
            .map(ActivityDto::fromDb)
            .map(ActivityItinerary::fromDto)
    }

    companion object {
        const val RESOURCE_NAME = "activity"

        private fun findById(tripId: Int, activityId: Int): Op<Boolean> {
            return (ActivityTable.id eq activityId) and (ActivityTable.trip eq tripId)
        }
    }
}