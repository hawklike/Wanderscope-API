package cz.cvut.fit.steuejan.travel.data.dao.forgotpassword

import cz.cvut.fit.steuejan.travel.data.database.InMemoryDatabase
import cz.cvut.fit.steuejan.travel.data.dto.ForgotPasswordDto
import cz.cvut.fit.steuejan.travel.data.model.Username
import java.util.*

class ForgotPasswordDaoImpl(private val database: InMemoryDatabase) : ForgotPasswordDao {
    override fun addForgotPassword(token: String, expiresAt: Date, username: Username): ForgotPasswordDto {
        val entity = ForgotPasswordDto(token, expiresAt, username)
        database.forgotPasswords.add(entity)
        return entity
    }

    override fun getForgotPassword(token: String): ForgotPasswordDto? {
        return database.forgotPasswords.find { it.token == token }
    }

    override fun deleteForgotPassword(token: String): Boolean {
        return database.forgotPasswords.removeIf { it.token == token }
    }
}