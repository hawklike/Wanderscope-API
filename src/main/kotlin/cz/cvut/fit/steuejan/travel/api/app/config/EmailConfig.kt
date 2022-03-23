package cz.cvut.fit.steuejan.travel.api.app.config

import io.ktor.config.*

class EmailConfig(override val config: ApplicationConfig) : AppConfig {
    val account = config.property(EMAIL_ACCOUNT).getString()
    val password = config.property(EMAIL_PASSWORD).getString()
    val host = config.property(EMAIL_HOST).getString()
    val port = config.property(EMAIL_PORT).getString().toInt()

    companion object {
        const val EMAIL_ACCOUNT = "email.account"
        const val EMAIL_PASSWORD = "email.password"
        const val EMAIL_HOST = "email.host"
        const val EMAIL_PORT = "email.port"
    }
}