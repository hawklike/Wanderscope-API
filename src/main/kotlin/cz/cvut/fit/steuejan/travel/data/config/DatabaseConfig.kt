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
        private const val HOST = "database.host"
        private const val PORT = "database.port"
        private const val NAME = "database.name"
        private const val USER = "database.user"
        private const val PASSWORD = "database.password"
        private const val MAX_POOL_SIZE = "database.maxPoolSize"

        const val NAME_LENGTH = 140
        const val PHONE_LENGTH = 31
        const val EMAIL_LENGTH = 254
        const val PASSWORD_LENGTH = 50
        const val USERNAME_LENGTH = 30
        const val LON_LAT_LENTGH = 15
        const val FILE_EXTENSION_LENGTH = 10
        const val DOCUMENT_KEY_LENTGH = 5
        const val TRIP_lINK_LENGTH = 8
    }
}