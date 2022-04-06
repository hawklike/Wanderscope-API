package cz.cvut.fit.steuejan.travel.api.app.plugin

import cz.cvut.fit.steuejan.travel.api.app.conversion.convertAuthFlow
import cz.cvut.fit.steuejan.travel.api.app.conversion.convertDateTime
import cz.cvut.fit.steuejan.travel.api.app.conversion.convertGetTripsType
import io.ktor.application.*
import io.ktor.features.*

fun Application.configureDataConversion() {
    install(DataConversion) {
        convertAuthFlow()
        convertGetTripsType()
        convertDateTime()
    }
}