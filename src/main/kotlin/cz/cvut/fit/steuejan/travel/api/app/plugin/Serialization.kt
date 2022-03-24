package cz.cvut.fit.steuejan.travel.api.app.plugin

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@ExperimentalSerializationApi
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
}
