package cz.cvut.fit.steuejan.travel.api.trip.itinerary.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.response.TripItineraryResponse

class ItineraryController(daoFactory: DaoFactory) : AbstractTripController(daoFactory) {

    suspend fun showItinerary(userId: Int, tripId: Int): Response {
        viewOrThrow(userId, tripId)
        val allPoints = with(daoFactory) {
            val transports = transportDao.showItinerary(tripId)
            val accommodation = accommodationDao.showItinerary(tripId)
            val activities = activityDao.showItinerary(tripId)
            val places = placeDao.showItinerary(tripId)
            transports + accommodation + activities + places
        }
        val itinerary = allPoints.sortedWith(compareBy(nullsLast()) { it.duration.startDate })
        return TripItineraryResponse.success(itinerary)
    }
}