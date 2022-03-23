package cz.cvut.fit.steuejan.travel.api.app.plugin

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

}
