package cz.cvut.fit.steuejan.travel.data.database.user

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("users") {
    val username = varchar("username", 30).uniqueIndex()
    val email = text("email")
}