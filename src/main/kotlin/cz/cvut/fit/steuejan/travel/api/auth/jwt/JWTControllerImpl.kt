package cz.cvut.fit.steuejan.travel.api.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import cz.cvut.fit.steuejan.travel.api.auth.token.AccessToken
import cz.cvut.fit.steuejan.travel.api.auth.token.RefreshToken
import cz.cvut.fit.steuejan.travel.api.auth.token.TokenHolder
import cz.cvut.fit.steuejan.travel.api.auth.token.TokenType
import cz.cvut.fit.steuejan.travel.data.dao.token.TokenDao
import cz.cvut.fit.steuejan.travel.data.model.Username
import java.util.*

class JWTControllerImpl(
    private val config: JWTConfig,
    private val tokenDao: TokenDao
) : JWTController {

    override fun getVerifier(tokenType: TokenType): JWTVerifier {
        return JWT.require(getAlgorithm(tokenType))
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .build()
    }

    override fun create(data: String, tokenType: TokenType): String {
        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withSubject(data)
            .withExpiresAt(Date(System.currentTimeMillis() + tokenType.expiration))
            .sign(getAlgorithm(tokenType))
    }

    override fun createTokens(username: Username): TokenHolder {
        val accessToken = create(username.it, tokenType = AccessToken())
        val refreshToken = create(username.it, tokenType = RefreshToken())
        tokenDao.addRefreshToken(refreshToken, username)
        return TokenHolder(accessToken, refreshToken)
    }

    private fun getAlgorithm(tokenType: TokenType): Algorithm {
        val secretKey = if (tokenType is AccessToken) {
            config.accessSecretKey
        } else {
            config.refreshSecretKey
        }
        return Algorithm.HMAC256(secretKey)
    }
}