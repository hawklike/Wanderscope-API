package cz.cvut.fit.steuejan.travel.data.database.tripuser

import cz.cvut.fit.steuejan.travel.data.database.trip.TripEntity
import cz.cvut.fit.steuejan.travel.data.database.trip.TripTable
import cz.cvut.fit.steuejan.travel.data.database.user.UserEntity
import cz.cvut.fit.steuejan.travel.data.database.user.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ColumnSet
import org.jetbrains.exposed.sql.ResultRow

class TripUserEntity(id: EntityID<Int>) : IntEntity(id) {
    //this code prevents from loading additional queries, see:
    //https://github.com/JetBrains/Exposed/issues/180#issuecomment-338137854
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