package cz.cvut.fit.steuejan.travel.api.auth.token

import kotlin.time.Duration.Companion.days

data class RefreshToken(override val expiration: Long = 90.days.inWholeMilliseconds) : TokenType(expiration)