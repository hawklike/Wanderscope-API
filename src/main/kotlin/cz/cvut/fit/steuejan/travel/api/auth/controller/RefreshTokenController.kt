package cz.cvut.fit.steuejan.travel.api.auth.controller

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import cz.cvut.fit.steuejan.travel.api.app.response.general.Response
import cz.cvut.fit.steuejan.travel.api.auth.exception.InvalidRefreshTokenException
import cz.cvut.fit.steuejan.travel.api.auth.exception.RefreshTokenExpiredException
import cz.cvut.fit.steuejan.travel.api.auth.jwt.JWTController
import cz.cvut.fit.steuejan.travel.api.auth.response.AuthResponse
import cz.cvut.fit.steuejan.travel.api.auth.token.RefreshToken
import cz.cvut.fit.steuejan.travel.data.dao.token.TokenDao
import cz.cvut.fit.steuejan.travel.data.model.Username

class RefreshTokenController(
    private val jwt: JWTController,
    private val tokenDao: TokenDao
) {

    fun refresh(refreshToken: String): Response {
        val username = validateJWT(refreshToken)
        //find refresh token in db
        tokenDao.findToken(refreshToken)?.let {
            //delete current refresh token from db
            tokenDao.deleteToken(it.refreshToken)
            //create new access and refresh tokens
            val tokens = jwt.createTokens(username)
            //save new refresh token to db
            tokenDao.addRefreshToken(tokens.refreshToken, username)
            //return both tokens
            return AuthResponse.success(tokens.accessToken, tokens.refreshToken)
        } ?: throw InvalidRefreshTokenException()
    }

    private fun validateJWT(refreshToken: String): Username {
        try {
            val token = jwt.getVerifier(RefreshToken()).verify(refreshToken)
            return Username(token.subject)
        } catch (ex: TokenExpiredException) {
            tokenDao.deleteToken(refreshToken)
            throw RefreshTokenExpiredException()
        } catch (ex: JWTVerificationException) {
            throw InvalidRefreshTokenException()
        }
    }

}