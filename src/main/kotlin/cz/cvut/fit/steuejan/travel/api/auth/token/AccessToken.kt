package cz.cvut.fit.steuejan.travel.api.auth.token

import kotlin.time.Duration.Companion.minutes

data class AccessToken(override val expiration: Long = 7.minutes.inWholeMilliseconds) : TokenType(expiration)