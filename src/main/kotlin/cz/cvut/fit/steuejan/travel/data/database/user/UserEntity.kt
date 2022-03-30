package cz.cvut.fit.steuejan.travel.data.database.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    var username by UserTable.username
    var accountType by UserTable.accountType
    var email by UserTable.email
    var password by UserTable.password
    var displayName by UserTable.displayName
    var deleted by UserTable.deleted
}