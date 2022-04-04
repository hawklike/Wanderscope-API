package cz.cvut.fit.steuejan.travel.data.database.forgotpassword.dao

import org.joda.time.DateTime

interface ForgotPasswordDao {
    suspend fun addForgotPassword(userId: Int, token: String, expiresAt: DateTime)
    suspend fun getForgotPassword(token: String): cz.cvut.fit.steuejan.travel.data.database.forgotpassword.ForgotPasswordDto?
    suspend fun deleteForgotPassword(token: String): Boolean
}