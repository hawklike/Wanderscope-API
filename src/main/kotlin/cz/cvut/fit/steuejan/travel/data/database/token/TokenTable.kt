package cz.cvut.fit.steuejan.travel.data.database.token

import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.jodatime.datetime

object TokenTable : LongIdTable("tokens") {
    val user = reference("user", UserTable, onDelete = CASCADE, onUpdate = CASCADE)

    val refreshToken = text("refresh_token").uniqueIndex()
    val created = datetime("created")
}