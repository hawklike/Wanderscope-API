package cz.cvut.fit.steuejan.travel.data.database

import cz.cvut.fit.steuejan.travel.data.config.Hikari
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceTable
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import cz.cvut.fit.steuejan.travel.data.dto.ForgotPasswordDto
import cz.cvut.fit.steuejan.travel.data.dto.TokenDto
import cz.cvut.fit.steuejan.travel.data.dto.UserDto
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class InMemoryDatabase {
    val users = mutableListOf<UserDto>()
    val tokens = mutableListOf<TokenDto>()
    val forgotPasswords = mutableListOf<ForgotPasswordDto>()
}

fun initDatabase(hikari: Hikari) {
    val tables = arrayOf(UserTable, TripTable, TripUserTable, PlaceTable)

    Database.connect(hikari.init())
    transaction {
        SchemaUtils.createMissingTablesAndColumns(*tables)
    }
}