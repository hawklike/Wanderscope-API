package cz.cvut.fit.steuejan.travel.data.dto

import cz.cvut.fit.steuejan.travel.data.model.Username
import java.util.*

//todo token has to be unique
data class ForgotPasswordDto(val token: String, val expiresAt: Date, val username: Username)
