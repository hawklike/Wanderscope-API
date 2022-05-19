package cz.cvut.fit.steuejan.travel.data.database.transport

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.model.TransportItinerary
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

@Suppress("DuplicatedCode")
class TransportDao : PointOfInterestDao<TransportDto> {
    override suspend fun add(tripId: Int, dto: TransportDto) = transaction {
        TransportTable.insertAndGetIdOrNull {
            it[trip] = tripId
            it[name] = dto.name
            it[type] = dto.type
            it[fromGooglePlaceId] = dto.from.googlePlaceId
            it[fromAddress] = dto.from.address
            it[toGooglePlaceId] = dto.to.googlePlaceId
            it[toAddress] = dto.to.address
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
            it[cars] = parseList(dto.cars)
            it[seats] = parseList(dto.seats)
            it[fromLatitude] = dto.fromCoordinates.latitude
            it[fromLongitude] = dto.fromCoordinates.longitude
            it[toLatitude] = dto.toCoordinates.latitude
            it[toLongitude] = dto.toCoordinates.longitude
        }?.value ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }

    override suspend fun find(tripId: Int, id: Int) = transaction {
        TransportTable.selectFirst { findById(tripId, id) }
    }?.let(TransportDto::fromDb)

    override suspend fun edit(tripId: Int, poiId: Int, dto: TransportDto) = transaction {
        TransportTable.updateOrNull({ findById(tripId, poiId) }) {
            it[name] = dto.name
            it[type] = dto.type
            it[fromGooglePlaceId] = dto.from.googlePlaceId
            it[fromAddress] = dto.from.address
            it[toGooglePlaceId] = dto.to.googlePlaceId
            it[toAddress] = dto.to.address
            it[description] = dto.description
            it[startDate] = dto.duration.startDate
            it[endDate] = dto.duration.endDate
            it[cars] = parseList(dto.cars)
            it[seats] = parseList(dto.seats)
            it[fromLatitude] = dto.fromCoordinates.latitude
            it[fromLongitude] = dto.fromCoordinates.longitude
            it[toLatitude] = dto.toCoordinates.latitude
            it[toLongitude] = dto.toCoordinates.longitude
        } ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }.isUpdated()

    override suspend fun delete(tripId: Int, poiId: Int) = transaction {
        TransportTable.deleteWhere { findById(tripId, poiId) }
    }.isDeleted()

    override suspend fun show(tripId: Int) = transaction {
        TransportTable.select { TransportTable.trip eq tripId }
            .orderBy(TransportTable.startDate, SortOrder.ASC_NULLS_LAST)
            .map(TransportDto::fromDb)
    }

    override suspend fun showItinerary(tripId: Int) = transaction {
        TransportTable.select { TransportTable.trip eq tripId }
            .map(TransportDto::fromDb)
            .map(TransportItinerary::fromDto)
    }

    companion object {
        const val RESOURCE_NAME = "transport"

        private fun findById(tripId: Int, transportId: Int): Op<Boolean> {
            return (TransportTable.id eq transportId) and (TransportTable.trip eq tripId)
        }
    }
}