package cz.cvut.fit.steuejan.travel.api.app

import cz.cvut.fit.steuejan.travel.api.app.plugin.*
import cz.cvut.fit.steuejan.travel.data.config.Hikari
import cz.cvut.fit.steuejan.travel.data.database.initDatabase
import io.ktor.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.ktor.ext.inject

lateinit var baseUrl: String

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@ExperimentalSerializationApi
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureBaseUrl()
    configureDI()
    configureStatusPages()
    configureDataConversion()
    configureAuthentication()
    configureSerialization()
    configureLocation()
    configureRouting()
    configureMonitoring()
    configureDB()
}

fun Application.configureBaseUrl() {
    baseUrl = environment.config.property("ktor.deployment.url").getString()
}

fun Application.configureDB() {
    val hikari: Hikari by inject()
    initDatabase(hikari)
}
