package cz.cvut.fit.steuejan.travel.api.app.di.factory

import cz.cvut.fit.steuejan.travel.api.account.controller.AccountController
import cz.cvut.fit.steuejan.travel.api.trip.controller.TripController

class ControllerFactory(
    val accountController: AccountController,
    val tripController: TripController
)