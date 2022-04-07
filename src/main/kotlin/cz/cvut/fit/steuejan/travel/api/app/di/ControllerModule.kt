package cz.cvut.fit.steuejan.travel.api.app.di

import cz.cvut.fit.steuejan.travel.api.account.controller.AccountController
import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.trip.controller.TripController
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.controller.AccomodationController
import cz.cvut.fit.steuejan.travel.api.trip.poi.activity.controller.ActivityController
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.controller.TransportController
import cz.cvut.fit.steuejan.travel.api.user.controller.UserController
import org.koin.dsl.module

val controllerModule = module {
    single { AccountController(get(), get(), get(), get()) }
    single { TripController(get()) }
    single { UserController(get()) }

    single { TransportController(get()) }
    single { AccomodationController(get()) }
    single { ActivityController(get()) }

    single { ControllerFactory(get(), get(), get(), get(), get(), get()) }
}