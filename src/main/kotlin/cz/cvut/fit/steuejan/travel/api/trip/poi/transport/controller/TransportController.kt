package cz.cvut.fit.steuejan.travel.api.trip.poi.transport.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.poi.controller.PointOfInterestController
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.response.ShowTransportsInTripResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.response.TransportResponse
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType

class TransportController(
    daoFactory: DaoFactory
) : PointOfInterestController<TransportDto>(daoFactory, daoFactory.transportDao) {
    override val notFound: String = FailureMessages.TRANSPORT_NOT_FOUND
    override val type = PointOfInterestType.TRANSPORT

    suspend fun getTransport(userId: Int, tripId: Int, transportId: Int): Response {
        return get(userId, tripId, transportId) as TransportResponse
    }

    suspend fun showTransportsInTrip(userId: Int, tripId: Int): Response {
        val transports = showInTrip(userId, tripId).map { it.toResponse() as TransportResponse }
        return ShowTransportsInTripResponse.success(transports)
    }
}