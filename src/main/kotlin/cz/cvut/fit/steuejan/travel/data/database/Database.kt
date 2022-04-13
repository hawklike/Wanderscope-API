package cz.cvut.fit.steuejan.travel.data.database

import cz.cvut.fit.steuejan.travel.data.config.Hikari
import cz.cvut.fit.steuejan.travel.data.database.accomodation.AccommodationTable
import cz.cvut.fit.steuejan.travel.data.database.activity.ActivityTable
import cz.cvut.fit.steuejan.travel.data.database.document.DocumentTable
import cz.cvut.fit.steuejan.travel.data.database.forgotpassword.ForgotPasswordTable
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceTable
import cz.cvut.fit.steuejan.travel.data.database.token.TokenTable
import cz.cvut.fit.steuejan.travel.data.database.transport.TransportTable
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDatabase(hikari: Hikari) {
    val tables = arrayOf(
        UserTable,
        TripTable,
        TripUserTable,
        PlaceTable,
        TransportTable,
        AccommodationTable,
        ActivityTable,
        DocumentTable,
        TokenTable,
        ForgotPasswordTable
    )

    Database.connect(hikari.init())
    transaction {
        SchemaUtils.createMissingTablesAndColumns(*tables)
    }
}