package cz.cvut.fit.steuejan.travel.api.app.plugin

import cz.cvut.fit.steuejan.travel.api.app.di.authModule
import cz.cvut.fit.steuejan.travel.api.app.di.configModule
import cz.cvut.fit.steuejan.travel.api.app.di.controllerModule
import cz.cvut.fit.steuejan.travel.api.app.di.daoModule
import io.ktor.application.*
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureDI() {
    install(Koin) {
        SLF4JLogger()
        modules(this@configureDI.configModule, daoModule, authModule, controllerModule)
    }
}