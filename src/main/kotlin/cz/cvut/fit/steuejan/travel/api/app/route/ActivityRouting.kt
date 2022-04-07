@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.route

import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.extension.getUserId
import cz.cvut.fit.steuejan.travel.api.app.extension.receive
import cz.cvut.fit.steuejan.travel.api.app.extension.respond
import cz.cvut.fit.steuejan.travel.api.app.location.Trip
import cz.cvut.fit.steuejan.travel.api.app.util.throwIfMissing
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTConfig
import cz.cvut.fit.steuejan.travel.api.trip.poi.activity.controller.ActivityController
import cz.cvut.fit.steuejan.travel.api.trip.poi.activity.request.ActivityRequest
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.activityRoutes() {
    val controllerFactory: ControllerFactory by inject()

    authenticate(JWTConfig.JWT_AUTHENTICATION) {
        val activityController = controllerFactory.activityController

        addActivity(activityController)
        showActivity(activityController)
        editActivity(activityController)
        deleteActivity(activityController)
    }
}

fun Route.addActivity(activityController: ActivityController) {
    post<Trip.Activity> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val activity = receive<ActivityRequest>(ActivityRequest.MISSING_PARAM).toDto()
        respond(activityController.add(getUserId(), tripId, activity))
    }
}

fun Route.showActivity(activityController: ActivityController) {
    get<Trip.Activity> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val activityId = it.activityId.throwIfMissing(it::activityId.name)
        respond(activityController.getActivity(getUserId(), tripId, activityId))
    }
}

fun Route.editActivity(activityController: ActivityController) {
    put<Trip.Activity> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val activityId = it.activityId.throwIfMissing(it::activityId.name)
        val activity = receive<ActivityRequest>(ActivityRequest.MISSING_PARAM).toDto()
        respond(activityController.edit(getUserId(), tripId, activityId, activity))
    }
}

fun Route.deleteActivity(activityController: ActivityController) {
    delete<Trip.Activity> {
        val tripId = it.trip.id.throwIfMissing(it.trip::id.name)
        val activityId = it.activityId.throwIfMissing(it::activityId.name)
        respond(activityController.delete(getUserId(), tripId, activityId))
    }
}



