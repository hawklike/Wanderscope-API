package cz.cvut.fit.steuejan.travel.api.app.plugin

import cz.cvut.fit.steuejan.travel.api.app.di.*
import io.ktor.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

@ExperimentalSerializationApi
fun Application.configureDI() {
    install(Koin) {
        SLF4JLogger()
        modules(
            this@configureDI.configModule,
            networkModule,
            daoModule,
            bussinessModule,
            authModule,
            controllerModule
        )
    }
}