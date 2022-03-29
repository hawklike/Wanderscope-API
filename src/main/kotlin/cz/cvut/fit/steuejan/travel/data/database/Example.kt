package cz.cvut.fit.steuejan.travel.data.database

import cz.cvut.fit.steuejan.travel.data.database.trip.TripEntity
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserEntity
import cz.cvut.fit.steuejan.travel.data.database.tripuser.TripUserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserEntity
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable.username
import cz.cvut.fit.steuejan.travel.data.model.Username
import cz.cvut.fit.steuejan.travel.data.util.doQuery
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

suspend fun addUser(username: String, email: String) {
    doQuery {
        UserEntity.new {
            this.username = username
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


    return doQuery {
        TripEntity.wrapRows(query).joinToString { it.name }
    }
}

suspend fun createTrip(name: String, owner: Username) {
    val user = doQuery {
        UserEntity.find { username eq owner.it }.first()
    }

    doQuery {
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
    doQuery {
        TripUserTable.deleteWhere { TripUserTable.trip eq tripId }
        TripEntity.findById(tripId)?.delete()
    }
}

