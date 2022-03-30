package cz.cvut.fit.steuejan.travel.data.database.user

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("users") {
    val username = varchar("username", 30).uniqueIndex()
    val accountType = enumerationByName("account_type", 8, AccountType::class)
    val email = text("email")
    val password = varchar("password", 50).nullable()
    val displayName = varchar("display_name", 30).nullable()
    val deleted = bool("deleted").default(false)
}