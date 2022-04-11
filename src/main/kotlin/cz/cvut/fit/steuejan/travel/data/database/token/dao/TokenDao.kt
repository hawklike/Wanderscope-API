package cz.cvut.fit.steuejan.travel.data.database.token.dao

import cz.cvut.fit.steuejan.travel.data.database.token.TokenDto

interface TokenDao {
    suspend fun addRefreshToken(userId: Int, refreshToken: String)
    suspend fun findToken(refreshToken: String): TokenDto?
    suspend fun deleteToken(refreshToken: String): Boolean
    suspend fun deleteTokensByUserId(userId: Int): Boolean
}