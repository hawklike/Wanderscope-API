package cz.cvut.fit.steuejan.travel.api.app.di.factory

import cz.cvut.fit.steuejan.travel.api.account.controller.AccountController
import cz.cvut.fit.steuejan.travel.api.trip.controller.TripController
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.controller.AccomodationController
import cz.cvut.fit.steuejan.travel.api.trip.poi.activity.controller.ActivityController
import cz.cvut.fit.steuejan.travel.api.trip.poi.place.controller.PlaceController
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.controller.TransportController
import cz.cvut.fit.steuejan.travel.api.user.controller.UserController

class ControllerFactory(
    val accountController: AccountController,
    val tripController: TripController,
    val userController: UserController,
    val transportController: TransportController,
    val accomodationController: AccomodationController,
    val activityController: ActivityController,
    val placeController: PlaceController
)