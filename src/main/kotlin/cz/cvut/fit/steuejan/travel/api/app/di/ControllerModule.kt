package cz.cvut.fit.steuejan.travel.api.app.di

import cz.cvut.fit.steuejan.travel.api.account.controller.AccountController
import cz.cvut.fit.steuejan.travel.api.app.di.factory.ControllerFactory
import org.koin.dsl.module

val controllerModule = module {
    single { AccountController(get(), get(), get(), get()) }

    single { ControllerFactory(get()) }
}