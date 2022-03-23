package cz.cvut.fit.steuejan.travel.api.app.config

import io.ktor.config.*

class LimitsConfig(override val config: ApplicationConfig) : AppConfig {
    val usernameLengthMin = config.property(USERNAME_MIN).getString().toInt()
    val usernameLengthMax = config.property(USERNAME_MAX).getString().toInt()
    val passwordLengthMin = config.property(PASSWORD_MIN).getString().toInt()
    val passwordLengthMax = config.property(PASSWORD_MAX).getString().toInt()

    companion object {
        const val USERNAME_MIN = "limit.username.min"
        const val USERNAME_MAX = "limit.username.max"
        const val PASSWORD_MIN = "limit.password.min"
        const val PASSWORD_MAX = "limit.password.max"
    }
}