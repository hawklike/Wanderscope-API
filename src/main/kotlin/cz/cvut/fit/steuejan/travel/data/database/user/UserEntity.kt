package cz.cvut.fit.steuejan.travel.data.database.user

import cz.cvut.fit.steuejan.travel.data.database.document.DocumentEntity
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    val documents by DocumentEntity referrersOn DocumentTable.owner

    var username by UserTable.username
    var accountType by UserTable.accountType
    var email by UserTable.email
    var password by UserTable.password
    var displayName by UserTable.displayName
    var deleted by UserTable.deleted
}