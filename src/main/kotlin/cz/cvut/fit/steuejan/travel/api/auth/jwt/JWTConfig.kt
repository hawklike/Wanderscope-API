package cz.cvut.fit.steuejan.travel.api.auth.jwt

import io.ktor.config.*

class JWTConfig(config: ApplicationConfig) {
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