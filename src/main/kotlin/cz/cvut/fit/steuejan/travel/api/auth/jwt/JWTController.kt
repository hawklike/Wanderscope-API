package cz.cvut.fit.steuejan.travel.api.auth.jwt

import com.auth0.jwt.JWTVerifier
import cz.cvut.fit.steuejan.travel.api.auth.token.TokenHolder
import cz.cvut.fit.steuejan.travel.api.auth.token.TokenType
import cz.cvut.fit.steuejan.travel.data.model.Username

interface JWTController {
    fun getVerifier(tokenType: TokenType): JWTVerifier
    fun create(data: String, tokenType: TokenType): String
    fun createTokens(username: Username): TokenHolder
}