package cz.cvut.fit.steuejan.travel.api.trip.points.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto

abstract class AbstractPointOfInterestController<T : PointOfInterestDto>(
    daoFactory: DaoFactory,
    protected val dao: PointOfInterestDao<T>
) : AbstractTripController(daoFactory) {

    abstract val notFound: String

    suspend fun add(userId: Int, tripId: Int, dto: T): Response {
        upsert(userId, tripId, dto) {
            dao.add(tripId, dto)
        }
        return Success(Status.CREATED)
    }

    suspend fun get(userId: Int, tripId: Int, poiId: Int): Response {
        return viewOrThrow(userId, tripId) {
            dao.find(tripId, poiId)?.toResponse() ?: throw NotFoundException(notFound)
        }!!
    }

    suspend fun delete(userId: Int, tripId: Int, poiId: Int): Response {
        editOrThrow(userId, tripId) {
            if (!dao.delete(poiId)) {
                throw NotFoundException(notFound)
            }
        }
        return Success(Status.NO_CONTENT)
    }

    suspend fun edit(userId: Int, tripId: Int, poiId: Int, dto: T): Response {
        upsert(userId, tripId, dto) {
            if (!dao.edit(poiId, dto)) {
                throw NotFoundException(notFound)
            }
        }
        return Success(Status.NO_CONTENT)
    }

    private suspend fun upsert(userId: Int, tripId: Int, dto: T, call: suspend () -> Unit) {
        if (dto.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }
        editOrThrow(userId, tripId) {
            call.invoke()
        }
    }
}