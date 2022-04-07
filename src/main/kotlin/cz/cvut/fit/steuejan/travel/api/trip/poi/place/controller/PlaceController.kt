package cz.cvut.fit.steuejan.travel.api.trip.poi.place.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.poi.controller.AbstractPointOfInterestController
import cz.cvut.fit.steuejan.travel.api.trip.poi.place.response.PlaceResponse
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceDto

class PlaceController(
    daoFactory: DaoFactory
) : AbstractPointOfInterestController<PlaceDto>(daoFactory, daoFactory.placeDao) {
    override val notFound: String = FailureMessages.PLACE_NOT_FOUND

    suspend fun getPlace(userId: Int, tripId: Int, placeId: Int): Response {
        return get(userId, tripId, placeId) as PlaceResponse
    }
}