package cz.cvut.fit.steuejan.travel.api.app.plugin

import cz.cvut.fit.steuejan.travel.api.app.di.*
import io.ktor.application.*
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureDI() {
    install(Koin) {
        SLF4JLogger()
        modules(this@configureDI.configModule, daoModule, bussinessModule, authModule, controllerModule)
    }
}