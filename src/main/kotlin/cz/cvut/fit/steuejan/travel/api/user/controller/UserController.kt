package cz.cvut.fit.steuejan.travel.api.user.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.model.TripOverview
import cz.cvut.fit.steuejan.travel.api.trip.response.TripOverviewListResponse

class UserController(private val daoFactory: DaoFactory) {

    suspend fun showTrips(userId: Int): Response {
        val trips = daoFactory.userDao.getTrips(userId)
        return TripOverviewListResponse.success(trips.map(TripOverview::fromDto))
    }
}