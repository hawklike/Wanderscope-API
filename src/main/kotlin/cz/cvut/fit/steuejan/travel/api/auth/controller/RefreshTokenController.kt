package cz.cvut.fit.steuejan.travel.api.auth.controller

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import cz.cvut.fit.steuejan.travel.api.app.response.Response
import cz.cvut.fit.steuejan.travel.api.auth.exception.InvalidRefreshTokenException
import cz.cvut.fit.steuejan.travel.api.auth.exception.RefreshTokenExpiredException
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTController
import cz.cvut.fit.steuejan.travel.api.auth.response.AuthResponse
import cz.cvut.fit.steuejan.travel.api.auth.token.RefreshToken
import cz.cvut.fit.steuejan.travel.data.database.token.dao.TokenDao

class RefreshTokenController(
    private val jwt: JWTController,
    private val tokenDao: TokenDao
) {
    suspend fun refresh(refreshToken: String): Response {
        val userId = validateJWT(refreshToken)
        //find refresh token in db
        tokenDao.findToken(refreshToken)?.let {
            //delete current refresh token from db
            tokenDao.deleteToken(it.refreshToken)
            //create new access and refresh tokens and save to db
            val tokens = jwt.createTokens(userId)
            //return both tokens
            return AuthResponse.success(tokens.accessToken, tokens.refreshToken)
        } ?: throw InvalidRefreshTokenException()
    }

    private suspend fun validateJWT(refreshToken: String): Int {
        try {
            val token = jwt.getVerifier(RefreshToken()).verify(refreshToken)
            return token.subject.toInt()
        } catch (ex: TokenExpiredException) {
            tokenDao.deleteToken(refreshToken)
            throw RefreshTokenExpiredException()
        } catch (ex: NumberFormatException) {
            throw InvalidRefreshTokenException()
        } catch (ex: JWTVerificationException) {
            throw InvalidRefreshTokenException()
        }
    }

}