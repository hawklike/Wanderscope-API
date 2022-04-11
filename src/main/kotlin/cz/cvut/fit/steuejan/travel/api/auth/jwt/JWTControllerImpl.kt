package cz.cvut.fit.steuejan.travel.api.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import cz.cvut.fit.steuejan.travel.api.auth.token.AccessToken
import cz.cvut.fit.steuejan.travel.api.auth.token.RefreshToken
import cz.cvut.fit.steuejan.travel.api.auth.token.TokenHolder
import cz.cvut.fit.steuejan.travel.api.auth.token.TokenType
import cz.cvut.fit.steuejan.travel.data.database.token.dao.TokenDao
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

    override suspend fun createTokens(userId: Int, addToDatabase: Boolean): TokenHolder {
        val accessToken = create(userId.toString(), tokenType = AccessToken())
        val refreshToken = create(userId.toString(), tokenType = RefreshToken())

        if (addToDatabase) {
            tokenDao.addRefreshToken(userId, refreshToken)
        }

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