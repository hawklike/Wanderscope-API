package cz.cvut.fit.steuejan.travel.api.app.config

import io.ktor.config.*

class DeploymentConfig(override val config: ApplicationConfig) : AppConfig {
    val baseUrl = config.property(BASE_URL).getString()
    val port = config.property(PORT).getString().toInt()

    companion object {
        private const val BASE_URL = "ktor.deployment.url"
        private const val PORT = "ktor.deployment.port"
    }
}