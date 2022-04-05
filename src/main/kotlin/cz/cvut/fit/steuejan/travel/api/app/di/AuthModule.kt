package cz.cvut.fit.steuejan.travel.api.app.di

import cz.cvut.fit.steuejan.travel.api.account.controller.AccountController
import cz.cvut.fit.steuejan.travel.api.app.bussines.EmailSender
import cz.cvut.fit.steuejan.travel.api.app.bussines.Validator
import cz.cvut.fit.steuejan.travel.api.app.di.factory.AuthControllerFactory
import cz.cvut.fit.steuejan.travel.api.app.di.factory.JWTControllerFactory
import cz.cvut.fit.steuejan.travel.api.auth.controller.EmailPasswordController
import cz.cvut.fit.steuejan.travel.api.auth.controller.RefreshTokenController
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTController
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTControllerImpl
import cz.cvut.fit.steuejan.travel.api.auth.util.ApiEncryptor
import cz.cvut.fit.steuejan.travel.api.auth.util.Encryptor
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    single { JWTControllerImpl(get(), get()) } bind JWTController::class

    factory { EmailSender(get()) }

    single { Validator(get(), get()) }

    single<Encryptor> { ApiEncryptor(config = get()) }

    single { AccountController(get(), get(), get(), get()) }
    single { EmailPasswordController(get(), get(), get(), get(), get()) }
    single { RefreshTokenController(get(), get()) }

    single { JWTControllerFactory(get()) }
    single { AuthControllerFactory(get(), get()) }
}