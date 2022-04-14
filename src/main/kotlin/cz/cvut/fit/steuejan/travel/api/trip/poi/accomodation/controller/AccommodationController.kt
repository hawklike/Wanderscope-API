package cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.response.AccommodationResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.response.ShowAccommodationInTripResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.controller.PointOfInterestController
import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccommodationDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType

class AccommodationController(
    daoFactory: DaoFactory
) : PointOfInterestController<AccommodationDto>(daoFactory, daoFactory.accommodationDao) {
    override val notFound = FailureMessages.ACCOMMODATION_NOT_FOUND
    override val type = PointOfInterestType.ACCOMMODATION

    suspend fun getAccommodation(userId: Int, tripId: Int, accommodationId: Int): Response {
        return get(userId, tripId, accommodationId) as AccommodationResponse
    }

    suspend fun showAccommodationInTrip(userId: Int, tripId: Int): Response {
        val accommodation = showInTrip(userId, tripId).map { it.toResponse() as AccommodationResponse }
        return ShowAccommodationInTripResponse.success(accommodation)
    }
}