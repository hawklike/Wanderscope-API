package cz.cvut.fit.steuejan.travel.data.database.forgotpassword.dao

import cz.cvut.fit.steuejan.travel.data.database.forgotpassword.ForgotPasswordDto
import cz.cvut.fit.steuejan.travel.data.database.forgotpassword.ForgotPasswordTable
import cz.cvut.fit.steuejan.travel.data.extension.isDeleted
import cz.cvut.fit.steuejan.travel.data.extension.selectFirst
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime

class ForgotPasswordDaoImpl : ForgotPasswordDao {

    override suspend fun addForgotPassword(userId: Int, token: String, expiresAt: DateTime) {
        transaction {
            ForgotPasswordTable.insert {
                it[this.user] = userId
                it[this.token] = token
                it[this.expiresAt] = expiresAt
            }
        }
    }

    override suspend fun getForgotPassword(token: String): ForgotPasswordDto? = transaction {
        ForgotPasswordTable.selectFirst { ForgotPasswordTable.token eq token }
    }?.let { ForgotPasswordDto.fromDb(it) }

    override suspend fun deleteForgotPassword(token: String) = transaction {
        ForgotPasswordTable.deleteWhere { ForgotPasswordTable.token eq token }
    }.isDeleted()
}