package cz.cvut.fit.steuejan.travel.api.app.di

import cz.cvut.fit.steuejan.travel.api.account.controller.AccountController
import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import cz.cvut.fit.steuejan.travel.api.trip.controller.TripController
import cz.cvut.fit.steuejan.travel.api.trip.document.controller.DocumentController
import cz.cvut.fit.steuejan.travel.api.trip.expense.controller.ExpenseController
import cz.cvut.fit.steuejan.travel.api.trip.expense.controller.ExpenseRoomController
import cz.cvut.fit.steuejan.travel.api.trip.itinerary.controller.ItineraryController
import cz.cvut.fit.steuejan.travel.api.trip.poi.accomodation.controller.AccommodationController
import cz.cvut.fit.steuejan.travel.api.trip.poi.activity.controller.ActivityController
import cz.cvut.fit.steuejan.travel.api.trip.poi.place.controller.PlaceController
import cz.cvut.fit.steuejan.travel.api.trip.poi.transport.controller.TransportController
import cz.cvut.fit.steuejan.travel.api.user.controller.UserController
import org.koin.dsl.module

val controllerModule = module {
    single { AccountController(get(), get(), get(), get()) }
    single { TripController(get()) }
    single { UserController(get()) }

    single { TransportController(get(), get()) }
    single { AccommodationController(get(), get()) }
    single { ActivityController(get(), get()) }
    single { PlaceController(get(), get()) }
    single { DocumentController(get(), get(), get(), get()) }

    single { ItineraryController(get()) }
    single { ExpenseRoomController(get(), get()) }
    single { ExpenseController(get()) }

    single { ControllerFactory(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}