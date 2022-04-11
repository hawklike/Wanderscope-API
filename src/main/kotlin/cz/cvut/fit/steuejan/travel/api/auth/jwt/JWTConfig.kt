package cz.cvut.fit.steuejan.travel.api.auth.jwt

import cz.cvut.fit.steuejan.travel.api.app.config.AppConfig
import io.ktor.config.*

class JWTConfig(override val config: ApplicationConfig) : AppConfig {
    val accessSecretKey = config.property(ACCESS_SECRET_KEY).getString()
    val refreshSecretKey = config.property(REFRESH_SECRET_KEY).getString()
    val audience = config.property(AUDIENCE).getString()
    val issuer = config.property(ISSUER).getString()

    companion object {
        private const val ACCESS_SECRET_KEY = "jwt.key.access"
        private const val REFRESH_SECRET_KEY = "jwt.key.refresh"
        private const val AUDIENCE = "jwt.audience"
        private const val ISSUER = "jwt.issuer"

        const val JWT_AUTHENTICATION = "jwt-auth"
    }
}