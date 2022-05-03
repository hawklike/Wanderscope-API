package cz.cvut.fit.steuejan.travel.api.user.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.app.util.parseBodyOrBadRequest
import cz.cvut.fit.steuejan.travel.api.trip.model.TripOverview
import cz.cvut.fit.steuejan.travel.api.trip.response.TripOverviewListResponse
import org.joda.time.DateTime
import org.joda.time.Duration

class UserController(private val daoFactory: DaoFactory) {

    suspend fun showAllTrips(userId: Int): Response {
        val trips = daoFactory.userDao.getAllTrips(userId)
        return TripOverviewListResponse.success(trips.map(TripOverview::fromDto))
    }

    suspend fun showUpcomingTrips(userId: Int, localTime: DateTime): Response {
        val trips = daoFactory.userDao.getUpcomingTrips(userId, localTime)
        return TripOverviewListResponse.success(trips.map(TripOverview::fromDto))
    }

    suspend fun showPastTrips(userId: Int, localTime: DateTime): Response {
        val trips = daoFactory.userDao.getPastTrips(userId, localTime)
        return TripOverviewListResponse.success(trips.map(TripOverview::fromDto))
    }

    suspend fun getRecommendedTrip(userId: Int, localTime: DateTime): Response {
        val upcomingTrips = daoFactory.userDao.getUpcomingTrips(userId, localTime)
        //find upcoming trip which has the startdate closest of the localTime
        val trip = upcomingTrips.minByOrNull { tripOverview ->
            val startDate = tripOverview.trip.duration.startDate ?: localTime.plusYears(299)
            val duration = parseBodyOrBadRequest(FailureMessages.RECOMMENDED_TRIP_ERROR) {
                Duration(startDate, localTime)
            }
            kotlin.math.abs(duration.standardMinutes)
        }
        val recommended = trip?.let { listOf(TripOverview.fromDto(it)) } ?: emptyList()
        return TripOverviewListResponse.success(recommended)
    }
}