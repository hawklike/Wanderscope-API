package cz.cvut.fit.steuejan.travel.api.trip.poi.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.BadRequestException
import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.CreatedResponse
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.response.Status
import cz.cvut.fit.steuejan.travel.api.app.response.Success
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.api.trip.document.model.DocumentOverview
import cz.cvut.fit.steuejan.travel.api.trip.document.response.DocumentOverviewListResponse
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType

abstract class PointOfInterestController<T : PointOfInterestDto>(
    daoFactory: DaoFactory,
    protected val dao: PointOfInterestDao<T>
) : AbstractTripController(daoFactory) {

    abstract val notFound: String
    abstract val type: PointOfInterestType

    suspend fun add(userId: Int, tripId: Int, dto: T): Response {
        val poiId = upsert(userId, tripId, dto) {
            dao.add(tripId, dto)
        }
        return CreatedResponse.success(poiId)
    }

    suspend fun get(userId: Int, tripId: Int, poiId: Int): Response {
        return viewOrThrow(userId, tripId) {
            dao.find(tripId, poiId)?.toResponse()
                ?: throw NotFoundException(notFound)
        }
    }

    suspend fun delete(userId: Int, tripId: Int, poiId: Int): Response {
        editOrThrow(userId, tripId) {
            if (!dao.delete(tripId, poiId)) {
                throw NotFoundException(notFound)
            }
        }
        return Success(Status.NO_CONTENT)
    }

    suspend fun edit(userId: Int, tripId: Int, poiId: Int, dto: T): Response {
        upsert(userId, tripId, dto) {
            if (!dao.edit(tripId, poiId, dto)) {
                throw NotFoundException(notFound)
            }
        }
        return Success(Status.NO_CONTENT)
    }

    open suspend fun showInTrip(userId: Int, tripId: Int): List<T> {
        viewOrThrow(userId, tripId)
        return dao.show(tripId)
    }

    suspend fun showDocuments(userId: Int, tripId: Int, poiId: Int): Response {
        viewOrThrow(userId, tripId)
        val documents = daoFactory.documentDao.getDocuments(tripId, poiId, this.type)
        return DocumentOverviewListResponse.success(documents.map(DocumentOverview::fromDto))
    }

    private suspend fun <R> upsert(userId: Int, tripId: Int, dto: T, call: suspend () -> R): R {
        if (dto.name.length > DatabaseConfig.NAME_LENGTH) {
            throw BadRequestException(FailureMessages.NAME_TOO_LONG)
        }
        return editOrThrow(userId, tripId) {
            call.invoke()
        }
    }
}