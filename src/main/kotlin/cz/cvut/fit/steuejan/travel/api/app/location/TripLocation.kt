@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package cz.cvut.fit.steuejan.travel.api.app.location

import cz.cvut.fit.steuejan.travel.api.trip.model.GetTripsType
import io.ktor.locations.*
import org.joda.time.DateTime

@Location("${Trip.URL}/{id}")
class Trip(val id: Int? = null) {

    @Location(Invite.URL)
    class Invite(val trip: Trip) {
        companion object {
            const val URL = "/invite"
        }
    }

    @Location(Transport.URL)
    class Transport(val trip: Trip) {
        companion object {
            const val URL = "/transport"
        }
    }

    companion object {
        const val URL = "/trip"
    }
}

@Location(Trips.URL)
class Trips(val scope: GetTripsType = GetTripsType.ALL, val date: DateTime? = null) {
    companion object {
        const val URL = "/trips"
    }
}