package cz.cvut.fit.steuejan.travel.api.trip.points.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.data.database.dao.PointOfInterestDao
import cz.cvut.fit.steuejan.travel.data.dto.PointOfInterestDto

abstract class AbstractPointOfInterestController<in T : PointOfInterestDto>(
    daoFactory: DaoFactory,
    protected val dao: PointOfInterestDao<T>
) : AbstractTripController(daoFactory) {

    suspend fun add(userId: Int, tripId: Int, dto: T): Response {
        dao.add(tripId, dto)
        TODO()
    }

    suspend fun delete(userId: Int, tripId: Int, poiId: Int): Response {
        dao.delete(poiId)
        TODO()
    }

    suspend fun edit(userId: Int, tripId: Int, dto: T): Response {
        dao.edit(tripId, dto)
        TODO()
    }
}