package cz.cvut.fit.steuejan.travel.api.trip.poi.place.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.poi.controller.PointOfInterestController
import cz.cvut.fit.steuejan.travel.api.trip.poi.place.response.PlaceResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.place.response.ShowPlacesInTripResponse
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType
import io.ktor.client.*

class PlaceController(
    daoFactory: DaoFactory,
    client: HttpClient
) : PointOfInterestController<PlaceDto>(daoFactory, client, daoFactory.placeDao) {
    override val notFound: String = FailureMessages.PLACE_NOT_FOUND
    override val type = PointOfInterestType.PLACE

    suspend fun getPlace(userId: Int, tripId: Int, placeId: Int): Response {
        return get(userId, tripId, placeId) as PlaceResponse
    }

    suspend fun showPlacesInTrip(userId: Int, tripId: Int): Response {
        val places = showInTrip(userId, tripId).map { it.toResponse() as PlaceResponse }
        return ShowPlacesInTripResponse.success(places)
    }
}