package cz.cvut.fit.steuejan.travel.api.trip.poi.activity.controller

import cz.cvut.fit.steuejan.travel.api.app.di.factory.DaoFactory
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.trip.poi.activity.response.ActivityResponse
import cz.cvut.fit.steuejan.travel.api.trip.poi.controller.AbstractPointOfInterestController
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityDto

class ActivityController(
    daoFactory: DaoFactory
) : AbstractPointOfInterestController<ActivityDto>(daoFactory, daoFactory.activityDao) {
    override val notFound: String = FailureMessages.ACTIVITY_NOT_FOUND

    suspend fun getActivity(userId: Int, tripId: Int, activityId: Int): Response {
        return get(userId, tripId, activityId) as ActivityResponse
    }
}