package cz.cvut.fit.steuejan.travel.api.app.plugin

import cz.cvut.fit.steuejan.travel.api.app.conversion.convertAuthFlow
import io.ktor.application.*
import io.ktor.features.*

fun Application.configureDataConversion() {
    install(DataConversion) {
        convertAuthFlow()
    }
}