package cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.response.AccomodationResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.controller.AbstractPointOfInterestController
import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccomodationDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType

class AccomodationController(
    daoFactory: DaoFactory
) : AbstractPointOfInterestController<AccomodationDto>(daoFactory, daoFactory.accomodationDao) {
    override val notFound = FailureMessages.ACCOMODATION_NOT_FOUND
    override val type = PointOfInterestType.ACCOMMODATION

    suspend fun getAccomodation(userId: Int, tripId: Int, accomodationId: Int): Response {
        return get(userId, tripId, accomodationId) as AccomodationResponse
    }
}