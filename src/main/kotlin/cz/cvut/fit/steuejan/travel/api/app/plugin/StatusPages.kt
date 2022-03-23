package cz.cvut.fit.steuejan.travel.api.app.plugin

import cz.cvut.fit.steuejan.travel.api.app.statuspages.authStatusPages
import cz.cvut.fit.steuejan.travel.api.app.statuspages.generalStatusPages
import io.ktor.application.*
import io.ktor.features.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        authStatusPages()
        generalStatusPages()
    }
}