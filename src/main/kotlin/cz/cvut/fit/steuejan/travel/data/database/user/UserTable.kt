package cz.cvut.fit.steuejan.travel.data.database.user

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.config.DatabaseConfig
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("users") {
    val username = varchar("username", DatabaseConfig.USERNAME_LENGTH).uniqueIndex()
    val accountType = enumerationByName("account_type", AccountType.MAX_LENGTH, AccountType::class)
    val email = varchar("email", DatabaseConfig.EMAIL_LENGTH)
    val password = text("password").nullable() //hashed with BCrypt
    val displayName = varchar("display_name", DatabaseConfig.USERNAME_LENGTH).nullable()
    val deleted = bool("deleted").default(false)

    init {
        index(isUnique = true, email, accountType)
    }
}