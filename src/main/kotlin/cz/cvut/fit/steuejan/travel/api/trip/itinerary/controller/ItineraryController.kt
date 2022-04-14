package cz.cvut.fit.steuejan.travel.api.trip.itinerary.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.controller.AbstractTripController
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.model.CommonItinerary
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.model.DateItinerary
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.response.TripItineraryResponse
import cz.cvut.fit.steuejan.travel.data.model.Duration
import org.joda.time.LocalDate

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
        val itineraryWithDates = addDateMarks(itinerary.toMutableList())
        return TripItineraryResponse.success(itineraryWithDates)
    }

    private fun addDateMarks(itinerary: MutableList<CommonItinerary>): List<CommonItinerary> {
        var lastDateModified = false
        var lastDate: LocalDate? = null
        val iterator = itinerary.listIterator()

        //add date marks before each individual days of some POIs
        for (item in iterator) {
            val currentDate = item.duration.startDate?.toLocalDate()
            if (currentDate != lastDate) {
                val duration = Duration(startDate = item.duration.startDate)
                iterator.addBefore(DateItinerary(duration))
                lastDate = currentDate
                lastDateModified = true
            }
        }

        //itinerary is not empty, but none of the POIs have date set
        //set date mark as empty
        if (!lastDateModified && itinerary.isNotEmpty()) {
            itinerary.add(0, DateItinerary(Duration()))
        }

        return itinerary
    }

    private fun <T> MutableListIterator<T>.addBefore(item: T) {
        this.previous()
        this.add(item)
        this.next()
    }
}