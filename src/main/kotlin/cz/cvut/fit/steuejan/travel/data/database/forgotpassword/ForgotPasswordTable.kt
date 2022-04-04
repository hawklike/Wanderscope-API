package cz.cvut.fit.steuejan.travel.data.database.forgotpassword

import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.jodatime.datetime

object ForgotPasswordTable : IntIdTable("forgot_passwords") {
    val user = reference("user", UserTable, onDelete = CASCADE, onUpdate = CASCADE)

    val token = text("token").uniqueIndex()
    val expiresAt = datetime("expires_at")
}