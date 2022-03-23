package cz.cvut.fit.steuejan.travel.data.dao.token

import cz.cvut.fit.steuejan.travel.data.database.InMemoryDatabase
import cz.cvut.fit.steuejan.travel.data.dto.TokenDto
import cz.cvut.fit.steuejan.travel.data.model.Username

class TokenDaoImp(private val database: InMemoryDatabase) : TokenDao {
    override fun addRefreshToken(refreshToken: String, username: Username): Boolean {
        return database.tokens.add(TokenDto(refreshToken, username.it))
    }

    override fun findToken(refreshToken: String): TokenDto? {
        return database.tokens.find { it.refreshToken == refreshToken }
    }

    override fun findTokensByUsername(username: Username): List<TokenDto> {
        return database.tokens.filter { it.username == username.it }
    }

    override fun deleteToken(id: String): Boolean {
        return database.tokens.removeIf { it.refreshToken == id }
    }

    override fun deleteTokensByUsername(username: Username): Boolean {
        return database.tokens.removeAll { it.username == username.it }
    }
}