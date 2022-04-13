package cz.cvut.fit.steuejan.travel.api.trip.poi.activity.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.poi.activity.response.ActivityResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.activity.response.ShowActivitiesInTripResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.controller.PointOfInterestController
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityDto
import cz.cvut.fit.steuejan.travel.data.model.PointOfInterestType

class ActivityController(
    daoFactory: DaoFactory
) : PointOfInterestController<ActivityDto>(daoFactory, daoFactory.activityDao) {
    override val notFound: String = FailureMessages.ACTIVITY_NOT_FOUND
    override val type = PointOfInterestType.ACTIVITY

    suspend fun getActivity(userId: Int, tripId: Int, activityId: Int): Response {
        return get(userId, tripId, activityId) as ActivityResponse
    }

    suspend fun showActivitiesInTrip(userId: Int, tripId: Int): Response {
        val activities = showInTrip(userId, tripId).map { it.toResponse() as ActivityResponse }
        return ShowActivitiesInTripResponse.success(activities)
    }
}