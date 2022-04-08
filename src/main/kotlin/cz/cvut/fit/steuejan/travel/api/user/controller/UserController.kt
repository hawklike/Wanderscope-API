package cz.cvut.fit.steuejan.travel.api.user.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.model.TripOverview
import cz.cvut.fit.steuejan.travel.api.trip.response.TripOverviewListResponse
import org.joda.time.DateTime

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
}