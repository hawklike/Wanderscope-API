package cz.cvut.fit.steuejan.travel.api.auth.jwt

import com.auth0.jwt.JWTVerifier
import cz.cvut.fit.steuejan.travel.api.auth.token.TokenHolder
import cz.cvut.fit.steuejan.travel.api.auth.token.TokenType

interface JWTController {
    fun getVerifier(tokenType: TokenType): JWTVerifier
    fun create(data: String, tokenType: TokenType): String
    suspend fun createTokens(userId: Int, addToDatabase: Boolean = true): TokenHolder
}