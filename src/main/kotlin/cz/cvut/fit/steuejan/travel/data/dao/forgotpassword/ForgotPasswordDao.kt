package cz.cvut.fit.steuejan.travel.data.dao.forgotpassword

import cz.cvut.fit.steuejan.travel.data.dto.ForgotPasswordDto
import cz.cvut.fit.steuejan.travel.data.model.Username
import java.util.*

interface ForgotPasswordDao {
    fun addForgotPassword(token: String, expiresAt: Date, username: Username): ForgotPasswordDto
    fun getForgotPassword(token: String): ForgotPasswordDto?
    fun deleteForgotPassword(token: String): Boolean
}