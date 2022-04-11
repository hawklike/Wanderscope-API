package cz.cvut.fit.steuejan.travel.data.database

import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceTable
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select

suspend fun addUser(username: String, email: String) {
    transaction {
        UserTable.insert {
            it[this.username] = username
            it[this.accountType] = AccountType.EMAIL
            it[this.email] = email
        }
    }
}

suspend fun getTrips(id: Int): String {
    val query = TripTable
        .innerJoin(TripUserTable)
        .innerJoin(UserTable)
        .slice(TripTable.columns)
        .select { UserTable.id eq id }
        .withDistinct()


    return transaction {
        query.joinToString { it[TripTable.name] }
    }
}

suspend fun createTrip(name: String, userId: Int) {
    transaction {
        val tripId = TripTable.insertAndGetId {
            it[this.name] = name
        }

        TripUserTable.insert {
            it[this.user] = userId
            it[this.trip] = tripId
        }
    }
}

suspend fun deleteTrip(tripId: Int) {
    transaction {
        TripTable.deleteWhere { TripTable.id eq tripId }
    }
}

suspend fun addPlace(tripId: Int, placeId: String, name: String) {
    transaction {
        PlaceTable.insert {
            it[this.name] = name
            it[this.googlePlaceId] = placeId
            it[this.trip] = tripId
        }
    }
}

suspend fun getPlaces(tripId: Int): String {
    val query = PlaceTable.select { PlaceTable.trip eq tripId }

    return transaction {
        query.joinToString { it[PlaceTable.googlePlaceId] ?: "" }
    }
}

suspend fun deletePlace(id: Int) {
    transaction {
        PlaceTable.deleteWhere { PlaceTable.id eq id }
    }
}

