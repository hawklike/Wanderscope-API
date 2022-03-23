package cz.cvut.fit.steuejan.travel.api.auth.token

sealed class TokenType(open val expiration: Long /* in milliseconds */)