package cz.cvut.fit.steuejan.travel.api.app.plugin

import io.ktor.application.*
import io.ktor.locations.*

fun Application.configureLocation() {
    install(Locations)
}