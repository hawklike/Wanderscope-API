package cz.cvut.fit.steuejan.travel.data.config

import cz.cvut.fit.steuejan.travel.api.app.config.AppConfig
import io.ktor.config.*

class DatabaseConfig(override val config: ApplicationConfig) : AppConfig {
    val host = config.property(HOST).getString()
    val port = config.property(PORT).getString()
    val name = config.property(NAME).getString()
    val user = config.property(USER).getString()
    val pass = config.property(PASSWORD).getString()
    val maxPoolSize = config.property(MAX_POOL_SIZE).getString().toInt()

    companion object {
        const val HOST = "database.host"
        const val PORT = "database.port"
        const val NAME = "database.name"
        const val USER = "database.user"
        const val PASSWORD = "database.password"
        const val MAX_POOL_SIZE = "database.maxPoolSize"
    }
}