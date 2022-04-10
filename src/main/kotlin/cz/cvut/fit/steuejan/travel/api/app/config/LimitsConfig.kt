package cz.cvut.fit.steuejan.travel.api.app.config

import io.ktor.config.*

class LimitsConfig(override val config: ApplicationConfig) : AppConfig {
    val usernameLengthMin = config.property(USERNAME_MIN).getString().toInt()
    val usernameLengthMax = config.property(USERNAME_MAX).getString().toInt()
    val passwordLengthMin = config.property(PASSWORD_MIN).getString().toInt()
    val passwordLengthMax = config.property(PASSWORD_MAX).getString().toInt()
    val documentKeyMin = config.property(DOCUMENT_KEY_MIN).getString().toInt()
    val documentKeyMax = config.property(DOCUMENT_KEY_MAX).getString().toInt()
    val documentMaxSize = config.property(DOCUMENT_MAX_SIZE).getString().toInt()

    companion object {
        private const val USERNAME_MIN = "limit.username.min"
        private const val USERNAME_MAX = "limit.username.max"
        private const val PASSWORD_MIN = "limit.password.min"
        private const val PASSWORD_MAX = "limit.password.max"
        private const val DOCUMENT_KEY_MIN = "limit.document.key.min"
        private const val DOCUMENT_KEY_MAX = "limit.document.key.max"
        private const val DOCUMENT_MAX_SIZE = "limit.document.maxSize"
    }
}