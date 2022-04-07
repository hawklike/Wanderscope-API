package cz.cvut.fit.steuejan.travel.data.database.transport

import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.extension.*
import cz.cvut.fit.steuejan.travel.data.util.transaction

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
        }?.value ?: throw BadRequestException(FailureMessages.ADD_TRANSPORT_FAILURE)
    }

    override suspend fun edit(poiId: Int, dto: TransportDto) = transaction {
        TransportTable.updateByIdOrNull(poiId) {
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
        } ?: throw BadRequestException(FailureMessages.ADD_TRANSPORT_FAILURE)
    }.isUpdated()

    override suspend fun delete(poiId: Int) = transaction {
        TransportTable.deleteById(poiId)
    }.isDeleted()

}