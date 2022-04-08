package cz.cvut.fit.steuejan.travel.api.trip.poi.transport.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.poi.controller.AbstractPointOfInterestController
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.response.TransportResponse
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportDto

class TransportController(
    daoFactory: DaoFactory
) : AbstractPointOfInterestController<TransportDto>(daoFactory, daoFactory.transportDao) {
    override val notFound: String = FailureMessages.TRANSPORT_NOT_FOUND

    suspend fun getTransport(userId: Int, tripId: Int, transportId: Int): Response {
        return get(userId, tripId, transportId) as TransportResponse
    }
}