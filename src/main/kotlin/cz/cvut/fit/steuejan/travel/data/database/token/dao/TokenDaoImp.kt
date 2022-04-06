package cz.cvut.fit.steuejan.travel.data.database.token.dao

import cz.cvut.fit.steuejan.travel.data.database.token.TokenDto
import cz.cvut.fit.steuejan.travel.data.database.token.TokenTable
import cz.cvut.fit.steuejan.travel.data.extension.isDeleted
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert

class TokenDaoImp : TokenDao {

    override suspend fun addRefreshToken(userId: Int, refreshToken: String) {
        transaction {
            TokenTable.insert {
                it[this.user] = userId
                it[this.refreshToken] = refreshToken
            }
        }
    }

    override suspend fun findToken(refreshToken: String) = transaction {
        TokenTable.selectFirst { TokenTable.refreshToken eq refreshToken }
    }?.let(TokenDto::fromDb)

    override suspend fun deleteToken(refreshToken: String) = transaction {
        TokenTable.deleteWhere { TokenTable.refreshToken eq refreshToken }
    }.isDeleted()

    override suspend fun deleteTokensByUserId(userId: Int) = transaction {
        TokenTable.deleteWhere { TokenTable.user eq userId }
    }.isDeleted()
}