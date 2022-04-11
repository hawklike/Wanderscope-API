package cz.cvut.fit.steuejan.travel.api.app.plugin

import cz.cvut.fit.steuejan.travel.api.app.route.*
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.routing.*

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureRouting() {
    routing {
        authRoutes()
        accountRoutes()
        tripRoutes()
        userRoutes()
        transportRoutes()
        accomodationRoutes()
        activityRoutes()
        placeRoutes()
        documentRoutes()

        exampleRoutes()
    }
}