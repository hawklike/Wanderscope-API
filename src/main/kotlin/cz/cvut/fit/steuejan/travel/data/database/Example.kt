package cz.cvut.fit.steuejan.travel.data.database

import cz.cvut.fit.steuejan.travel.data.database.UserTable.username
import cz.cvut.fit.steuejan.travel.data.model.Username
import cz.cvut.fit.steuejan.travel.data.util.doQuery
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ColumnSet
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime


object UserTable : IntIdTable("users") {
    val username = varchar("username", 30).uniqueIndex()
    val email = text("email")
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    var username by UserTable.username
    var email by UserTable.email

    val trips by TripUserEntity referrersOn TripUserTable.user
}

object TripTable : IntIdTable("trips") {
    val name = text("name")
    val startDate = datetime("start_date").default(DateTime.now())
}

class TripEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TripEntity>(TripTable)

    var name by TripTable.name
    var startDate by TripTable.startDate

    val users by TripUserEntity referrersOn TripUserTable.trip
}

object TripUserTable : IntIdTable() {
    val user = reference("user", UserTable)
    val trip = reference("trip", TripTable)
    val canEdit = bool("can_edit").default(true)
}

class TripUserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TripUserEntity>(TripUserTable) {
        override val dependsOnTables: ColumnSet
            get() = UserTable.innerJoin(TripUserTable).innerJoin(TripTable)

        override fun createInstance(entityId: EntityID<Int>, row: ResultRow?): TripUserEntity {
            row?.getOrNull(UserTable.id)?.let {
                UserEntity.wrap(it, row)
            }
            row?.getOrNull(TripTable.id)?.let {
                TripEntity.wrap(it, row)
            }
            return super.createInstance(entityId, row)
        }
    }

    var user by UserEntity referencedOn TripUserTable.user
    var trip by TripEntity referencedOn TripUserTable.trip
    var canEdit by TripUserTable.canEdit
}

suspend fun addUser(username: String, email: String) {
    doQuery {
        UserEntity.new {
            this.username = username
            this.email = email
        }
    }
}

suspend fun getTrips(username: String): String {
    return doQuery {
        val user = UserEntity.find { UserTable.username eq username }.first()
        user.trips.joinToString { it.trip.name }
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

suspend fun deleteTrip(id: Int) {
    doQuery {
        TripEntity.findById(id)?.delete()
    }
}

