package cz.cvut.fit.steuejan.travel.data.database.transport

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

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
        }?.value ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }

    override suspend fun find(tripId: Int, id: Int) = transaction {
        TransportTable.selectFirst {
            (TransportTable.trip eq tripId) and (TransportTable.id eq id)
        }
    }?.let(TransportDto::fromDb)

    override suspend fun edit(tripId: Int, poiId: Int, dto: TransportDto) = transaction {
        TransportTable.updateOrNull({
            (TransportTable.id eq poiId) and (TransportTable.trip eq tripId)
        }) {
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
        } ?: throw BadRequestException(FailureMessages.poiDbInsertionFailure(RESOURCE_NAME))
    }.isUpdated()

    override suspend fun delete(tripId: Int, poiId: Int) = transaction {
        TransportTable.deleteWhere {
            (TransportTable.id eq poiId) and (TransportTable.trip eq tripId)
        }
    }.isDeleted()

    companion object {
        const val RESOURCE_NAME = "transport"
    }
}