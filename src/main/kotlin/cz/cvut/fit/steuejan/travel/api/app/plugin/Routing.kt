package cz.cvut.fit.steuejan.travel.api.app.plugin

import cz.cvut.fit.steuejan.travel.api.app.route.accountRoutes
import cz.cvut.fit.steuejan.travel.api.app.route.authRoutes
import cz.cvut.fit.steuejan.travel.api.app.route.exampleRoutes
import cz.cvut.fit.steuejan.travel.api.app.route.tripRoutes
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.routing.*

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureRouting() {
    routing {
        authRoutes()
        accountRoutes()
        tripRoutes()
        exampleRoutes()
    }
}