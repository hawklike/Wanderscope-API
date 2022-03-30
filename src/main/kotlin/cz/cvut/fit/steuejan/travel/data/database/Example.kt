package cz.cvut.fit.steuejan.travel.data.database

import cz.cvut.fit.steuejan.travel.api.app.exception.NotFoundException
import cz.cvut.fit.steuejan.travel.api.app.exception.message.FailureMessages
import cz.cvut.fit.steuejan.travel.api.auth.model.AccountType
import cz.cvut.fit.steuejan.travel.data.database.place.PlaceEntity
import cz.cvut.fit.steuejan.travel.data.database.trip.TripEntity
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserEntity
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserEntity
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable.username
import cz.cvut.fit.steuejan.travel.data.model.Username
import cz.cvut.fit.steuejan.travel.data.util.transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

suspend fun addUser(username: String, email: String) {
    transaction {
        UserEntity.new {
            this.username = username
            this.accountType = AccountType.EMAIL
            this.email = email
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
        TripEntity.wrapRows(query).joinToString { it.name }
    }
}

suspend fun createTrip(name: String, owner: Username) {
    val user = transaction {
        UserEntity.find { username eq owner.it }.first()
    }

    transaction {
        val trip = TripEntity.new {
            this.name = name
        }

        TripUserEntity.new {
            this.user = user
            this.trip = trip
        }
    }
}

suspend fun deleteTrip(tripId: Int) {
    val trip = transaction {
        TripEntity.findById(tripId)
            ?: throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)
    }

    transaction {
        TripUserTable.deleteWhere { TripUserTable.trip eq tripId }
        trip.places.forEach { it.delete() }
        trip.delete()
    }
}

suspend fun addPlace(tripId: Int, placeId: String, name: String) {
    val trip = transaction {
        TripEntity.findById(tripId)
            ?: throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)
    }

    transaction {
        PlaceEntity.new {
            this.trip = trip
            this.googlePlaceId = placeId
            this.name = name
        }
    }
}

suspend fun getPlaces(tripId: Int): String {
    return transaction {
        val trip = TripEntity.findById(tripId)
            ?: throw NotFoundException(FailureMessages.TRIP_NOT_FOUND)

        trip.places.joinToString { it.googlePlaceId ?: "" }
    }
}

suspend fun deletePlace(id: Int) {
    transaction {
        PlaceEntity.findById(id)?.delete()
    }
}

