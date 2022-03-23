package cz.cvut.fit.steuejan.travel.data.dao.token

import cz.cvut.fit.steuejan.travel.data.dto.TokenDto
import cz.cvut.fit.steuejan.travel.data.model.Username

interface TokenDao {
    fun addRefreshToken(refreshToken: String, username: Username): Boolean
    fun findToken(refreshToken: String): TokenDto?
    fun findTokensByUsername(username: Username): List<TokenDto>
    fun deleteToken(id: String): Boolean
    fun deleteTokensByUsername(username: Username): Boolean
}